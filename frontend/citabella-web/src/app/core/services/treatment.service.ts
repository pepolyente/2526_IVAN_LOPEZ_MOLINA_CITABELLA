import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TreatmentRequest, TreatmentResponse } from '../../shared/models/treatment.model';

@Injectable({ providedIn: 'root' })
export class TreatmentService {

  private readonly BASE = '/api/treatments';

  constructor(private http: HttpClient) {}

  getAll(): Observable<TreatmentResponse[]> {
    return this.http.get<TreatmentResponse[]>(this.BASE);
  }

  getById(id: number): Observable<TreatmentResponse> {
    return this.http.get<TreatmentResponse>(`${this.BASE}/${id}`);
  }

  create(request: TreatmentRequest): Observable<TreatmentResponse> {
    return this.http.post<TreatmentResponse>(this.BASE, request);
  }
}
