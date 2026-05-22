import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  ProductPublicResponse,
  ProductPrivateResponse,
  ProductRequest
} from '../../shared/models/product.model';
import { PageResponse } from '../../shared/models/page-response.model';
import {buildHttpParams} from '../utils/http-params.util';
import {SearchableQueryParams} from '../../shared/models/query-params.model';

@Injectable({ providedIn: 'root' })
export class ProductService {

  private readonly BASE = '/api/products';

  constructor(private http: HttpClient) {}

  /** GET /api/products */
  getAllActive(): Observable<ProductPublicResponse[]> {
    return this.http.get<ProductPublicResponse[]>(this.BASE);
  }

  /** GET /api/products/admin */
  getAdmin(
    params?: SearchableQueryParams
  ): Observable<PageResponse<ProductPrivateResponse>> {

    return this.http.get<PageResponse<ProductPrivateResponse>>(
      `${this.BASE}/admin`,
      {
        params: buildHttpParams(params)
      }
    );
  }

  getById(id: number): Observable<ProductPrivateResponse> {
    return this.http.get<ProductPrivateResponse>(`${this.BASE}/${id}`);
  }

  create(body: ProductRequest): Observable<ProductPrivateResponse> {
    return this.http.post<ProductPrivateResponse>(this.BASE, body);
  }

  update(id: number, body: ProductRequest): Observable<ProductPrivateResponse> {
    return this.http.put<ProductPrivateResponse>(`${this.BASE}/${id}`, body);
  }

  /** DELETE /api/products/{id} */
  deactivate(id: number): Observable<ProductPrivateResponse> {
    return this.http.delete<ProductPrivateResponse>(`${this.BASE}/${id}`);
  }

  activate(id: number): Observable<ProductPrivateResponse> {
    return this.http.patch<ProductPrivateResponse>(`${this.BASE}/${id}/activate`, {});
  }

  delete(id: number): Observable<ProductPrivateResponse> {
    return this.http.delete<ProductPrivateResponse>(`${this.BASE}/${id}`);
  }
}
