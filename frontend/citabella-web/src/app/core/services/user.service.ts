import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserInfoResponse } from '../../shared/models/auth.model';
import { UserResponse, UserUpdateRequest } from '../../shared/models/user.model';
import { PageResponse } from '../../shared/models/page-response.model';
import {buildHttpParams} from '../utils/http-params.util';
import {UserQueryParams} from '../../shared/models/query-params.model';

@Injectable({ providedIn: 'root' })
export class UserService {

  private readonly BASE = '/api/users';

  constructor(private http: HttpClient) {}

  getAll(
    params?: UserQueryParams
  ): Observable<PageResponse<UserResponse>> {

    return this.http.get<PageResponse<UserResponse>>(
      this.BASE,
      {
        params: buildHttpParams(params)
      }
    );
  }

  getById(id: number): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.BASE}/${id}`);
  }

  create(body: any): Observable<UserResponse> {
    return this.http.post<UserResponse>(this.BASE, body);
  }

  update(id: number, body: UserUpdateRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.BASE}/${id}`, body);
  }

  /** Bloquea lógicamente al usuario (LOCKED) — DELETE /api/users/{id} */
  deactivate(id: number): Observable<UserResponse> {
    return this.http.delete<UserResponse>(`${this.BASE}/${id}`);
  }

  delete(id: number): Observable<UserResponse> {
    return this.http.delete<UserResponse>(`${this.BASE}/${id}`);
  }

  swapRole(userId: number, roleName: string): Observable<UserInfoResponse> {
    return this.http.patch<UserInfoResponse>(
      `${this.BASE}/${userId}/swap-role/${roleName}`, {}
    );
  }
}
