import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  AppointmentResponse,
  CreateAppointmentRequest,
  RescheduleAppointmentRequest
} from '../../shared/models/appointment.model';
import { PageResponse } from '../../shared/models/page-response.model';

@Injectable({ providedIn: 'root' })
export class AppointmentService {

  private readonly BASE = '/api/appointments';

  constructor(private http: HttpClient) {}

  getAll(params?: {
    page?: number;
    size?: number;
    sort?: string[];
    status?: string;
  }): Observable<PageResponse<AppointmentResponse>> {

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

    return this.http.get<PageResponse<AppointmentResponse>>(this.BASE, { params: httpParams });
  }

  getById(id: number): Observable<AppointmentResponse> {
    return this.http.get<AppointmentResponse>(`${this.BASE}/${id}`);
  }

  create(request: CreateAppointmentRequest): Observable<AppointmentResponse> {
    return this.http.post<AppointmentResponse>(this.BASE, request);
  }

  update(request: RescheduleAppointmentRequest): Observable<AppointmentResponse> {
    return this.http.put<AppointmentResponse>(`${this.BASE}/update`, request);
  }

  changeStatus(id: number, status: string): Observable<AppointmentResponse> {
    return this.http.patch<AppointmentResponse>(
      `${this.BASE}/${id}/status`,
      { status }
    );
  }

  cancel(id: number): Observable<AppointmentResponse> {
    return this.http.delete<AppointmentResponse>(`${this.BASE}/${id}`);
  }

  getMyAppointments(params?: { page?: number; size?: number }): Observable<PageResponse<AppointmentResponse>> {
    let httpParams = new HttpParams();
    if (params?.page !== undefined) httpParams = httpParams.set('page', params.page);
    if (params?.size !== undefined) httpParams = httpParams.set('size', params.size);
    return this.http.get<PageResponse<AppointmentResponse>>(`${this.BASE}/my`, { params: httpParams });
  }
}
