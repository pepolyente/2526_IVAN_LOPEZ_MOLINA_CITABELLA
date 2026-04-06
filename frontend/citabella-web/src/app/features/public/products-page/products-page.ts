import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../core/services/product.service';
import { ProductPublicResponse } from '../../../shared/models/product.model';

@Component({
  selector: 'app-products-page',
  standalone: false,
  template: `
    <div class="page-wrapper">
      <h1>Nuestros productos</h1>
      <div class="cards-grid">
        @for (product of products; track product.id) {
          <div class="card">
            <h3>{{ product.name }}</h3>
            <p>{{ product.category }}</p>
            <strong>{{ product.salePrice | currency: 'EUR' }}</strong>
          </div>
        }
      </div>
    </div>
  `,
})
export class ProductsPage implements OnInit {
  products: ProductPublicResponse[] = [];
  constructor(private svc: ProductService) {}
  ngOnInit(): void { this.svc.getAllActive().subscribe(data => (this.products = data)); }
}
