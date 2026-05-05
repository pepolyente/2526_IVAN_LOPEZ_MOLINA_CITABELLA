import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ProductService } from '../../../core/services/product.service';
import { ProductPublicResponse } from '../../../shared/models/product.model';

@Component({
  selector: 'app-products-page',
  standalone: false,
  template: `
    <div class="page-wrapper">
      <h1>Nuestros productos</h1>
      @if (loading) {
        <p class="empty-state">Cargando...</p>
      } @else if (products.length === 0) {
        <p class="empty-state">Próximamente...</p>
      } @else {
        <div class="cards-grid">
          @for (product of products; track product.id) {
            <div class="card">
              <img
                [src]="product.imageKey || placeholder"
                [alt]="product.name"
                class="card-img"
                (error)="$any($event.target).src = placeholder"
              />
              <h3>{{ product.name }}</h3>
              <p>{{ product.category }}</p>
              <strong>{{ product.salePrice | currency:'EUR' }}</strong>
            </div>
          }
        </div>
      }
    </div>
  `,
})
export class ProductsPage implements OnInit {
  products: ProductPublicResponse[] = [];
  loading = true;
  readonly placeholder = '/images/citabella.jpg';

  constructor(private svc: ProductService, private changeDetectorRef: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.svc.getAllActive().subscribe({
      next: data => { this.products = data; this.loading = false; this.changeDetectorRef.detectChanges(); },
      error: () => { this.loading = false; this.changeDetectorRef.detectChanges(); }
    });
  }
}
