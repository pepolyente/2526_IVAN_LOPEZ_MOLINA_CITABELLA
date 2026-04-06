import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  AppointmentResponse,
  CreateAppointmentRequest,
} from '../../shared/models/appointment.model';

@Injectable({ providedIn: 'root' })
export class AppointmentService {

  private readonly BASE = '/api/appointments';

  constructor(private http: HttpClient) {}

  create(request: CreateAppointmentRequest): Observable<AppointmentResponse> {
    return this.http.post<AppointmentResponse>(this.BASE, request);
  }

  getAll(): Observable<AppointmentResponse[]> {
    return this.http.get<AppointmentResponse[]>(this.BASE);
  }
}
