import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserInfoResponse } from '../../shared/models/auth.model';
import { UserResponse } from '../../shared/models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {

  private readonly BASE = '/api/users';

  constructor(private http: HttpClient) {}

  getAll(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.BASE);
  }

  swapRole(userId: number, roleName: string): Observable<UserInfoResponse> {
    return this.http.patch<UserInfoResponse>(
      `${this.BASE}/${userId}/swap-role/${roleName}`, {}
    );
  }
}
