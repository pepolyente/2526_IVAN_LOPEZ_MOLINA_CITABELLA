import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ProductService } from '../../../core/services/product.service';
import { ProductPrivateResponse, ProductRequest } from '../../../shared/models/product.model';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-product-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Gestión de productos</h2>
      <button class="btn-primary" (click)="showCreateForm = !showCreateForm">
        {{ showCreateForm ? 'Cancelar' : '+ Nuevo producto' }}
      </button>
    </div>

    @if (showCreateForm) {
      <form class="form-card create-form" (ngSubmit)="createProduct()">
        <h3>Nuevo producto</h3>
        <div class="form-row">
          <div class="form-group">
            <label>Nombre *</label>
            <input type="text" [(ngModel)]="newProduct.name" name="name" required />
          </div>
          <div class="form-group">
            <label>Categoría</label>
            <input type="text" [(ngModel)]="newProduct.category" name="category" />
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>Precio compra (€)</label>
            <input type="number" [(ngModel)]="newProduct.purchasePrice" name="purchasePrice" min="0" step="0.01" />
          </div>
          <div class="form-group">
            <label>Precio venta (€)</label>
            <input type="number" [(ngModel)]="newProduct.salePrice" name="salePrice" min="0" step="0.01" />
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>Tipo de uso</label>
            <select [(ngModel)]="newProduct.usageType" name="usageType">
              <option value="">Sin especificar</option>
              <option value="INTERNAL">Uso interno</option>
              <option value="SALE">Venta</option>
              <option value="BOTH">Ambos</option>
            </select>
          </div>
          <div class="form-group">
            <label>Proveedor</label>
            <input type="text" [(ngModel)]="newProduct.supplier" name="supplier" />
          </div>
        </div>
        <div class="form-group check-group">
          <label>
            <input type="checkbox" [(ngModel)]="newProduct.isCritical" name="isCritical" />
            Producto crítico (stock mínimo)
          </label>
        </div>
        @if (createError) { <p class="error-msg">{{ createError }}</p> }
        <button type="submit" class="btn-primary" [disabled]="creating || !newProduct.name">
          {{ creating ? 'Guardando...' : 'Crear producto' }}
        </button>
      </form>
    }

    <div class="filters-bar">
      <div class="filter-group">
        <label>Estado</label>
        <select [(ngModel)]="filterActive" (change)="applyFilter()">
          <option [ngValue]="undefined">Todos</option>
          <option [ngValue]="true">Activos</option>
          <option [ngValue]="false">Inactivos</option>
        </select>
      </div>

      <div class="filter-group search-group">
        <label>Buscar producto</label>
        <div class="search-wrapper">
          <input type="text"
                 [(ngModel)]="searchTerm"
                 placeholder="Nombre o categoría..."
                 (keyup.enter)="onSearch()"
                 class="inline-input" />
          <button class="btn-outline" (click)="onSearch()">Buscar</button>
          @if (searchTerm) {
            <button class="btn-outline" (click)="clearSearch()">✕</button>
          }
        </div>
      </div>


    </div>
    <span class="total-hint">{{ totalElements }} producto(s)</span>
    @if (loading) {
      <div class="skeleton-table">
        @for (i of [1,2,3,4,5]; track i) {
          <div class="skeleton-row">
            <div class="skeleton-cell sk-wide"></div>
            <div class="skeleton-cell sk-medium"></div>
            <div class="skeleton-cell sk-narrow"></div>
          </div>
        }
      </div>
    } @else if (products.length === 0) {
      <p class="empty-state">No hay productos.</p>
    } @else {
      <div class="table-wrapper">
        <table class="simple-table">
          <thead>
          <tr>
            <th>Nombre</th>
            <th>Categoría</th>
            <th>P. Compra</th>
            <th>P. Venta</th>
            <th>Proveedor</th>
            <th>Crítico</th>
            <th>Activo</th>
            <th>Acciones</th>
          </tr>
          </thead>
          <tbody>
            @for (p of products; track p.id) {
              <tr [class.row-inactive]="!p.active">
                @if (editingId === p.id) {
                  <td data-label="Nombre"><input [(ngModel)]="editForm.name" class="inline-input" /></td>
                  <td data-label="Categoría"><input [(ngModel)]="editForm.category" class="inline-input" /></td>
                  <td data-label="P. Compra"><input type="number" step="0.01" [(ngModel)]="editForm.purchasePrice" class="inline-input" /></td>
                  <td data-label="P. Venta"><input type="number" step="0.01" [(ngModel)]="editForm.salePrice" class="inline-input" /></td>
                  <td data-label="Proveedor"><input [(ngModel)]="editForm.supplier" class="inline-input" /></td>
                  <td data-label="Crítico" class="cell-center">{{ p.isCritical ? '⚠️' : '' }}</td>
                  <td data-label="Activo">
                  <span class="badge" [class.badge-confirmed]="p.active" [class.badge-cancelled]="!p.active">
                    {{ p.active ? 'Activo' : 'Inactivo' }}
                  </span>
                  </td>
                  <td data-label="Acciones" class="actions-cell">
                    <button class="btn-xs btn-success" (click)="saveEdit(p.id)">✓</button>
                    <button class="btn-xs btn-outline" (click)="cancelEdit()">✕</button>
                  </td>
                } @else {
                  <td data-label="Nombre">{{ p.name }}</td>
                  <td data-label="Categoría">{{ p.category }}</td>
                  <td data-label="P. Compra">{{ p.purchasePrice }}</td>
                  <td data-label="P. Venta">{{ p.salePrice }}</td>
                  <td data-label="Proveedor">{{ p.supplier }}</td>
                  <td data-label="Crítico" class="cell-center">{{ p.isCritical ? '⚠️' : '' }}</td>
                  <td data-label="Activo">
                  <span class="badge" [class.badge-confirmed]="p.active" [class.badge-cancelled]="!p.active">
                    {{ p.active ? 'Activo' : 'Inactivo' }}
                  </span>
                  </td>
                  <td data-label="Acciones" class="actions-cell">
                    <button class="btn-xs btn-outline" (click)="startEdit(p)"><span class="material-symbols-outlined">edit</span></button>
                    @if (p.active) {
                      <button class="btn-xs btn-danger" (click)="deactivate(p.id)"><span class="material-symbols-outlined">person_off</span></button>
                    }
                  </td>
                }
              </tr>
            }
          </tbody>
        </table>
      </div>

      <div class="paginator">
        <button class="btn-outline" (click)="prevPage()" [disabled]="page === 0">← Anterior</button>
        <span class="page-info">Pág. {{ page + 1 }} / {{ totalPages }}</span>
        <button class="btn-outline" (click)="nextPage()" [disabled]="page >= totalPages - 1">Siguiente →</button>
      </div>
    }
  `,
})
export class ProductList implements OnInit {
  products:      ProductPrivateResponse[] = [];
  loading        = true;
  page           = 0;
  size           = 20;
  totalPages     = 0;
  totalElements  = 0;
  filterActive: boolean | undefined = undefined;
  searchTerm = '';

  showCreateForm = false;
  newProduct: ProductRequest = { name: '' };
  creating    = false;
  createError = '';
  editingId: number | null = null;
  editForm: ProductRequest = { name: '' };

  constructor(
    private svc: ProductService,
    private changeDetectorRef: ChangeDetectorRef,
    private toast: ToastService,
    private confirmSvc: ConfirmService
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.svc.getAdmin({ page: this.page, size: this.size, active: this.filterActive, search: this.searchTerm || undefined }).subscribe({
      next: (p: any) => {
        this.products      = p.content;
        this.totalPages    = p.totalPages;
        this.totalElements = p.totalElements;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => { this.loading = false; this.changeDetectorRef.detectChanges(); },
    });
  }

  onSearch(): void {
    this.page = 0;
    this.load();
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.onSearch();
  }

  applyFilter(): void { this.page = 0; this.load(); }
  prevPage(): void { if (this.page > 0) { this.page--; this.load(); } }
  nextPage(): void { if (this.page < this.totalPages - 1) { this.page++; this.load(); } }

  createProduct(): void {
    this.creating    = true;
    this.createError = '';
    this.svc.create(this.newProduct).subscribe({
      next: () => {
        this.creating       = false;
        this.showCreateForm = false;
        this.newProduct     = { name: '' };
        this.load();
      },
      error: err => {
        this.createError = err.error?.message ?? 'Error al crear el producto';
        this.creating    = false;
        this.changeDetectorRef.detectChanges();
      },
    });
  }

  async deactivate(id: number): Promise<void> {
    const ok = await this.confirmSvc.confirm('¿Desactivar este producto?');
    if (!ok) return;
    this.svc.deactivate(id).subscribe({ next: () => this.load() });
  }

  startEdit(p: ProductPrivateResponse): void {
    this.editingId = p.id;
    this.editForm = {
      name: p.name,
      category: p.category,
      purchasePrice: p.purchasePrice,
      salePrice: p.salePrice,
      supplier: p.supplier,
      isCritical: p.isCritical,
    };
  }

  cancelEdit(): void { this.editingId = null; }

  saveEdit(id: number): void {
    this.svc.update(id, this.editForm).subscribe({
      next: () => {
        this.toast.show('Producto actualizado');
        this.cancelEdit();
        this.load();
      },
      error: err => this.toast.show(err.error?.message ?? 'Error', 'error'),
    });
  }
}
