import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  ProductPublicResponse,
  ProductPrivateResponse,
} from '../../shared/models/product.model';

@Injectable({ providedIn: 'root' })
export class ProductService {

  private readonly BASE = '/api/products';

  constructor(private http: HttpClient) {}

  getAllActive(): Observable<ProductPublicResponse[]> {
    return this.http.get<ProductPublicResponse[]>(this.BASE);
  }

  getById(id: number): Observable<ProductPrivateResponse> {
    return this.http.get<ProductPrivateResponse>(`${this.BASE}/${id}`);
  }
}
