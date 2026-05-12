import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmployeeRequest, EmployeeResponse } from '../../shared/models/employee.model';
import { PageResponse } from '../../shared/models/page-response.model';

@Injectable({ providedIn: 'root' })
export class EmployeeService {

  private readonly BASE = '/api/employees';

  constructor(private http: HttpClient) {}

  getAll(params?: {
    page?: number;
    size?: number;
    sort?: string[];
    active?: boolean;
  }): Observable<PageResponse<EmployeeResponse>> {

    let httpParams = new HttpParams();

    Object.entries(params || {}).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        if (Array.isArray(value)) {
          value.forEach(v => httpParams = httpParams.append(key, v));
        } else {
          httpParams = httpParams.set(key, value);
        }
      }
    });

    return this.http.get<PageResponse<EmployeeResponse>>(this.BASE, { params: httpParams });
  }

  getById(id: number): Observable<EmployeeResponse> {
    return this.http.get<EmployeeResponse>(`${this.BASE}/${id}`);
  }

  create(body: EmployeeRequest): Observable<EmployeeResponse> {
    return this.http.post<EmployeeResponse>(this.BASE, body);
  }

  update(id: number, body: EmployeeRequest): Observable<EmployeeResponse> {
    return this.http.put<EmployeeResponse>(`${this.BASE}/${id}`, body);
  }

  delete(id: number): Observable<EmployeeResponse> {
    return this.http.delete<EmployeeResponse>(`${this.BASE}/${id}`);
  }

  activate(id: number): Observable<EmployeeResponse> {
    return this.http.patch<EmployeeResponse>(`${this.BASE}/${id}/activate`, {});
  }

  linkUser(employeeId: number, userId: number): Observable<void> {
    return this.http.patch<void>(`${this.BASE}/${employeeId}/link-user/${userId}`, {});
  }
}
