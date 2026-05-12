import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  TreatmentDetailedResponse,
  TreatmentRequest,
  TreatmentResponse
} from '../../shared/models/treatment.model';
import { PageResponse } from '../../shared/models/page-response.model';

@Injectable({ providedIn: 'root' })
export class TreatmentService {

  private readonly BASE = '/api/treatments';

  constructor(private http: HttpClient) {}

  /** GET /api/treatments */
  getAll(params?: {
    page?: number;
    size?: number;
    sort?: string[];
  }): Observable<PageResponse<TreatmentResponse>> {

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

    return this.http.get<PageResponse<TreatmentResponse>>(this.BASE, { params: httpParams });
  }

  /** GET /api/treatments/detail */
  getDetailed(params?: {
    page?: number;
    size?: number;
    sort?: string[];
    active?: boolean;
  }): Observable<PageResponse<TreatmentDetailedResponse>> {

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

    return this.http.get<PageResponse<TreatmentDetailedResponse>>(
      `${this.BASE}/detail`,
      { params: httpParams }
    );
  }

  getById(id: number): Observable<TreatmentResponse> {
    return this.http.get<TreatmentResponse>(`${this.BASE}/${id}`);
  }

  create(body: TreatmentRequest): Observable<TreatmentResponse> {
    return this.http.post<TreatmentResponse>(this.BASE, body);
  }

  update(id: number, body: TreatmentRequest): Observable<TreatmentResponse> {
    return this.http.put<TreatmentResponse>(`${this.BASE}/${id}`, body);
  }

  /** DELETE /api/treatments/{id} */
  deactivate(id: number): Observable<TreatmentResponse> {
    return this.http.delete<TreatmentResponse>(`${this.BASE}/${id}`);
  }

  delete(id: number): Observable<TreatmentResponse> {
    return this.http.delete<TreatmentResponse>(`${this.BASE}/${id}`);
  }
}
