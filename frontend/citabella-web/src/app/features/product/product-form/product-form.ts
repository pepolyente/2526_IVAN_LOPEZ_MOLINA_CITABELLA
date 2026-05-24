import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ProductService } from '../../../core/services/product.service';
import { ProductRequest } from '../../../shared/models/product.model';

@Component({
  selector: 'app-product-form',
  standalone: false,
  template: `
    <div class="page-header modal-create">
      <h2>Nuevo producto</h2>
      <button class="btn-outline" (click)="router.navigate(['/panel/products'])">Cancelar</button>
    </div>
    <form class="form-card" (ngSubmit)="submit()">
      <div class="form-row">
        <div class="form-group">
          <label>Nombre *</label>
          <input type="text" [(ngModel)]="form.name" name="name" required />
        </div>
        <div class="form-group">
          <label>Categoría</label>
          <input type="text" [(ngModel)]="form.category" name="category" />
        </div>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>Precio compra (€)</label>
          <input type="number" [(ngModel)]="form.purchasePrice" name="purchasePrice" min="0" step="0.01" />
        </div>
        <div class="form-group">
          <label>Precio venta (€)</label>
          <input type="number" [(ngModel)]="form.salePrice" name="salePrice" min="0" step="0.01" />
        </div>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>Tipo de uso</label>
          <select [(ngModel)]="form.usageType" name="usageType">
            <option value="" disabled selected hidden>
              Sin especificar
            </option>
            <option value="INTERNAL">Uso interno</option>
            <option value="SALE">Venta</option>
            <option value="BOTH">Ambos</option>
          </select>
        </div>
        <div class="form-group">
          <label>Proveedor</label>
          <input type="text" [(ngModel)]="form.supplier" name="supplier" />
        </div>
      </div>
      <div class="form-group check-group">
        <label>
          <input type="checkbox" [(ngModel)]="form.isCritical" name="isCritical" />
          Producto crítico (stock mínimo)
        </label>
      </div>
      @if (error) { <p class="error-msg">{{ error }}</p> }
      <button type="submit" class="btn-primary" [disabled]="loading">
        {{ loading ? 'Guardando...' : 'Crear producto' }}
      </button>
    </form>
  `
})
export class ProductForm {
  form: ProductRequest = { name: '' };
  loading = false;
  error = '';

  constructor(private svc: ProductService, public router: Router) {}

  submit(): void {
    this.loading = true;
    this.svc.create(this.form).subscribe({
      next: () => this.router.navigate(['/panel/products']),
      error: err => {
        this.error = err.error?.message ?? 'Error al crear el producto';
        this.loading = false;
      }
    });
  }
}
