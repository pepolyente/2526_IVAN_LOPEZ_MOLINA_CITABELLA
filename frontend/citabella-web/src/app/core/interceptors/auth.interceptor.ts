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

const AUTH_API_ENDPOINTS = ['/api/auth/login', '/api/auth/register'];
const PUBLIC_FRONTEND_ROUTES = ['/login', '/register'];

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private handlingUnauthorized = false;

  // Router y ToastService son seguros — ninguno depende de HttpClient.
  // NO inyectamos AuthService para evitar la dependencia circular:
  //   AuthInterceptor → AuthService → HttpClient → HTTP_INTERCEPTORS → AuthInterceptor
  constructor(
    private router: Router,
    private toast: ToastService,
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = localStorage.getItem('token');

    const cloned = token
      ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
      : req;

    return next.handle(cloned).pipe(
      catchError((error: HttpErrorResponse) => {
        const isAuthError = error.status === 401 || error.status === 403;
        const isAuthApiCall = AUTH_API_ENDPOINTS.some(ep => req.url.includes(ep));

        if (isAuthError && !isAuthApiCall) {
          this.handleSessionExpired();
        }

        return throwError(() => error);
      }),
    );
  }

  private handleSessionExpired(): void {
    if (this.handlingUnauthorized) return;
    this.handlingUnauthorized = true;

    // Inlineamos el logout para no depender de AuthService
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('username');
    localStorage.removeItem('userId');

    const currentUrl = this.router.url;
    const isAlreadyPublic = PUBLIC_FRONTEND_ROUTES.some(r =>
      currentUrl === r || currentUrl.startsWith(r + '?'),
    );

    if (!isAlreadyPublic) {
      this.toast.show('Se ha cerrado la sesión', 'warning');
      this.router.navigate(['/login']);
    }

    setTimeout(() => { this.handlingUnauthorized = false; }, 2000);
  }
}
