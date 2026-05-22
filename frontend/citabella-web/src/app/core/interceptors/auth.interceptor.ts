import { Injectable } from '@angular/core';
import {
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpErrorResponse,
} from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { ToastService } from '../services/toast.service';

const AUTH_API_ENDPOINTS  = ['/api/auth/login', '/api/auth/register'];
const PUBLIC_FRONTEND_ROUTES = ['/login', '/register'];

/**
 * Comportamiento:
 *  • 401 fuera de endpoints de auth → token caducado/inválido → logout + redirige a /login
 *  • 403 fuera de endpoints de auth → acceso denegado por rol → redirige a /
 *                                     sin cerrar la sesión
 *  • Cualquier otro error            → se propaga tal cual
 */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  /** Evita tratar varios errores en cascada dentro de la misma "ráfaga" */
  private handlingUnauthorized = false;
  private handlingForbidden    = false;

  // Router y ToastService son seguros — ninguno depende de HttpClient.
  // NO inyectamos AuthService para evitar la dependencia circular:
  //   AuthInterceptor → AuthService → HttpClient → HTTP_INTERCEPTORS → AuthInterceptor
  constructor(
    private router: Router,
    private toast:  ToastService,
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = localStorage.getItem('token');

    const cloned = token
      ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
      : req;

    return next.handle(cloned).pipe(
      catchError((error: HttpErrorResponse) => {
        const isAuthApiCall = AUTH_API_ENDPOINTS.some(ep => req.url.includes(ep));

        if (!isAuthApiCall) {
          if (error.status === 401) {
            // Token caducado o inválido → cerrar sesión
            this.handleSessionExpired();
          } else if (error.status === 403) {
            // El usuario está autenticado pero no tiene permiso para este recurso.
            // NO se cierra la sesión; simplemente se le redirige al inicio.
            this.handleForbidden();
          }
        }

        return throwError(() => error);
      }),
    );
  }

  // ── Handlers privados ──────────────────────────────────────────────────────

  /** 401: sesión expirada → logout completo y redirige a /login. */
  private handleSessionExpired(): void {
    if (this.handlingUnauthorized) return;
    this.handlingUnauthorized = true;

    // Inline del logout para no depender de AuthService (evita circular)
    ['token', 'role', 'username', 'userId'].forEach(k => localStorage.removeItem(k));

    const currentUrl = this.router.url;
    const isAlreadyPublic = PUBLIC_FRONTEND_ROUTES.some(
      r => currentUrl === r || currentUrl.startsWith(r + '?'),
    );

    if (!isAlreadyPublic) {
      this.toast.show('Tu sesión ha caducado. Por favor, vuelve a iniciar sesión.', 'warning');
      this.router.navigate(['/login']);
    }

    setTimeout(() => { this.handlingUnauthorized = false; }, 2000);
  }

  /**
   * 403: el usuario NO tiene permiso para este recurso.
   * Se mantiene la sesión activa; solo se redirige a la página principal.
   */
  private handleForbidden(): void {
    if (this.handlingForbidden) return;
    this.handlingForbidden = true;

    const currentUrl = this.router.url;
    const isInPanel = currentUrl.startsWith('/panel');

    if (isInPanel) {
      this.toast.show('No tienes permisos para acceder a este recurso.', 'warning');
      this.router.navigate(['/']);
    }

    setTimeout(() => { this.handlingForbidden = false; }, 2000);
  }
}
