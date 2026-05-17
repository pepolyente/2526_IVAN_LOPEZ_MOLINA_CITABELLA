import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ProductService } from '../../../core/services/product.service';
import { ProductPublicResponse } from '../../../shared/models/product.model';

@Component({
  selector: 'app-products-page',
  standalone: false,
  template: `
    <div class="products-page-wrapper">
      <div class="products-page-header">
        <span class="products-eyebrow">Nuestra tienda</span>
        <h1 class="products-page-title">Productos exclusivos</h1>
        <p class="products-page-sub">Los mejores cosméticos y productos de belleza seleccionados por nuestras especialistas.</p>
      </div>

      @if (loading) {
        <div class="products-skeleton-grid">
          @for (i of [1,2,3,4,5,6]; track i) {
            <div class="product-skeleton-card">
              <div class="skel-img"></div>
              <div class="skel-line skel-wide" style="margin: 12px 16px 0;"></div>
              <div class="skel-line skel-narrow" style="margin: 8px 16px 16px;"></div>
            </div>
          }
        </div>
      } @else if (products.length === 0) {
        <div class="products-empty">
          <span class="material-symbols-outlined">inventory_2</span>
          <p>Próximamente más productos</p>
        </div>
      } @else {
        <div class="products-catalog-grid">
          @for (product of products; track product.id) {
            <div class="product-catalog-card">
              <div class="pcc-img-wrap">
                <img
                  [src]="product.imageKey || placeholder"
                  [alt]="product.name"
                  class="pcc-img"
                  (error)="$any($event.target).src = placeholder"
                />
                @if (product.category) {
                  <span class="pcc-category-badge">{{ product.category }}</span>
                }
              </div>
              <div class="pcc-body">
                <div class="pcc-name">{{ product.name }}</div>
                <div class="pcc-price">{{ product.salePrice | currency:'EUR' }}</div>
              </div>
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
