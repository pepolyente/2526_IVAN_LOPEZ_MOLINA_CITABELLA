import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { switchMap, map } from 'rxjs/operators';
import {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  UserInfoResponse,
} from '../../shared/models/auth.model';

const SESSION_KEYS = ['token', 'role', 'username', 'userId'] as const;

/** Roles que conceden acceso al panel privado de la aplicación. */
const PANEL_ROLES = ['CLIENT', 'EMPLOYEE', 'ADMIN'] as const;
type PanelRole = typeof PANEL_ROLES[number];

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly BASE = '/api/auth';

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.BASE}/login`, request).pipe(
      tap(response => {
        localStorage.setItem('token',    response.token);
        localStorage.setItem('role',     response.role);
        localStorage.setItem('username', response.username);
      }),
      switchMap(() => this.me()),
      tap(info => {
        localStorage.setItem('role',     info.role);
        localStorage.setItem('username', info.username);
        localStorage.setItem('userId',   String(info.id));
      }),
      map(() => ({
        token:    this.getToken()!,
        username: this.getUsername(),
        role:     this.getRole(),
      })),
    );
  }

  register(request: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.BASE}/register`, request);
  }

  me(): Observable<UserInfoResponse> {
    return this.http.get<UserInfoResponse>(`${this.BASE}/me`);
  }

  // ── Getters ──────────────────────────────────────────────────────────────

  getToken(): string | null { return localStorage.getItem('token'); }
  getRole(): string         { return localStorage.getItem('role')     ?? ''; }
  getUsername(): string     { return localStorage.getItem('username') ?? ''; }
  getName(): string         { return this.getUsername(); }

  isLogged(): boolean { return !!this.getToken(); }

  hasRole(...roles: string[]): boolean {
    return roles.includes(this.getRole());
  }

  /**
   * Devuelve `true` si el usuario tiene uno de los roles reconocidos
   * que permiten acceder al panel privado (CLIENT, EMPLOYEE, ADMIN).
   *
   * Úsalo para condicionar el botón "Panel" en la cabecera pública
   * y para las redirecciones post-login.
   */
  hasPanelAccess(): boolean {
    return (PANEL_ROLES as readonly string[]).includes(this.getRole());
  }

  /**
   * Devuelve la ruta del panel correspondiente al rol del usuario.
   * Retorna `null` si el usuario no tiene un rol con acceso al panel.
   */
  getPanelRoute(): string | null {
    const role = this.getRole() as PanelRole | string;
    if (role === 'ADMIN' || role === 'EMPLOYEE') return '/panel/appointments';
    if (role === 'CLIENT')                       return '/panel/my-appointments';
    return null;
  }

  // ── Session management ────────────────────────────────────────────────────

  /**
   * Elimina todos los datos de sesión del localStorage.
   * Llamado desde el botón de logout manual.
   * El interceptor lo invoca inline para evitar dependencia circular.
   */
  logout(): void {
    SESSION_KEYS.forEach(key => localStorage.removeItem(key));
  }
}
