import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClientRequest, ClientResponse } from '../../shared/models/client.model';

@Injectable({ providedIn: 'root' })
export class ClientService {

  private readonly BASE = '/api/clients';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ClientResponse[]> {
    return this.http.get<ClientResponse[]>(this.BASE);
  }

  getById(id: number): Observable<ClientResponse> {
    return this.http.get<ClientResponse>(`${this.BASE}/${id}`);
  }

  create(request: ClientRequest): Observable<ClientResponse> {
    return this.http.post<ClientResponse>(this.BASE, request);
  }

  linkUser(clientId: number, userId: number): Observable<void> {
    return this.http.patch<void>(`${this.BASE}/${clientId}/link-user/${userId}`, {});
  }

  unlinkUser(clientId: number): Observable<void> {
    return this.http.patch<void>(`${this.BASE}/${clientId}/unlink-user`, {});
  }
}
