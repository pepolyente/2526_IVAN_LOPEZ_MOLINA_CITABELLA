import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmployeeRequest, EmployeeResponse } from '../../shared/models/employee.model';
import { PageResponse } from '../../shared/models/page-response.model';
import {SearchableQueryParams} from '../../shared/models/query-params.model';
import {buildHttpParams} from '../utils/http-params.util';

@Injectable({ providedIn: 'root' })
export class EmployeeService {

  private readonly BASE = '/api/employees';

  constructor(private http: HttpClient) {}

  getAll(
    params?: SearchableQueryParams
  ): Observable<PageResponse<EmployeeResponse>> {

    return this.http.get<PageResponse<EmployeeResponse>>(
      this.BASE,
      {
        params: buildHttpParams(params)
      }
    );
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
