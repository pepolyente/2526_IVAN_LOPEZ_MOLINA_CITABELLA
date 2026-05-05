import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClientRequest, ClientResponse } from '../../shared/models/client.model';
import { PageResponse } from '../../shared/models/page-response.model';

@Injectable({ providedIn: 'root' })
export class ClientService {

  private readonly BASE = '/api/clients';

  constructor(private http: HttpClient) {}

  getAll(params?: {
    page?: number;
    size?: number;
    sort?: string[];
    active?: boolean;
  }): Observable<PageResponse<ClientResponse>> {

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

    return this.http.get<PageResponse<ClientResponse>>(this.BASE, { params: httpParams });
  }

  getById(id: number): Observable<ClientResponse> {
    return this.http.get<ClientResponse>(`${this.BASE}/${id}`);
  }

  create(body: ClientRequest): Observable<ClientResponse> {
    return this.http.post<ClientResponse>(this.BASE, body);
  }

  update(id: number, body: ClientRequest): Observable<ClientResponse> {
    return this.http.put<ClientResponse>(`${this.BASE}/${id}`, body);
  }

  /** DELETE /api/clients/{id} */
  deactivate(id: number): Observable<ClientResponse> {
    return this.http.delete<ClientResponse>(`${this.BASE}/${id}`);
  }

  delete(id: number): Observable<ClientResponse> {
    return this.http.delete<ClientResponse>(`${this.BASE}/${id}`);
  }

  linkUser(clientId: number, userId: number): Observable<void> {
    return this.http.patch<void>(`${this.BASE}/${clientId}/link-user/${userId}`, {});
  }

  unlinkUser(clientId: number): Observable<void> {
    return this.http.patch<void>(`${this.BASE}/${clientId}/unlink-user`, {});
  }
}
