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
  getRole(): string { return localStorage.getItem('role')     ?? ''; }
  getUsername(): string { return localStorage.getItem('username') ?? ''; }
  getName(): string { return this.getUsername(); }

  isLogged(): boolean { return !!this.getToken(); }

  hasRole(...roles: string[]): boolean {
    return roles.includes(this.getRole());
  }

  // ── Session management ────────────────────────────────────────────────────

  /**
   * Elimina todos los datos de sesión del localStorage.
   * Llamado tanto desde el botón de logout manual como desde
   * AuthInterceptor al recibir un 401/403.
   */
  logout(): void {
    SESSION_KEYS.forEach(key => localStorage.removeItem(key));
  }
}
