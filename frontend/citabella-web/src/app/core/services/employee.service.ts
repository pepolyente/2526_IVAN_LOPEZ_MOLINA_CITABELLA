import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmployeeRequest, EmployeeResponse } from '../../shared/models/employee.model';

@Injectable({ providedIn: 'root' })
export class EmployeeService {

  private readonly BASE = '/api/employees';

  constructor(private http: HttpClient) {}

  getAll(): Observable<EmployeeResponse[]> {
    return this.http.get<EmployeeResponse[]>(this.BASE);
  }

  getById(id: number): Observable<EmployeeResponse> {
    return this.http.get<EmployeeResponse>(`${this.BASE}/${id}`);
  }

  create(request: EmployeeRequest): Observable<EmployeeResponse> {
    return this.http.post<EmployeeResponse>(this.BASE, request);
  }

  linkUser(employeeId: number, userId: number): Observable<void> {
    return this.http.patch<void>(`${this.BASE}/${employeeId}/link-user/${userId}`, {});
  }
}
