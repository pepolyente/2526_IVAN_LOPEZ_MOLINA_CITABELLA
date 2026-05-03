import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  UserInfoResponse,
} from '../../shared/models/auth.model';

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
      })
    );
  }

  register(request: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.BASE}/register`, request);
  }

  me(): Observable<UserInfoResponse> {
    return this.http.get<UserInfoResponse>(`${this.BASE}/me`);
  }

  getToken():    string | null { return localStorage.getItem('token'); }
  getRole():     string        { return localStorage.getItem('role')     ?? ''; }
  getUsername(): string        { return localStorage.getItem('username') ?? ''; }

  getName(): string { return this.getUsername(); }

  isLogged(): boolean { return !!this.getToken(); }

  hasRole(...roles: string[]): boolean {
    return roles.includes(this.getRole());
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('username');
  }
}
