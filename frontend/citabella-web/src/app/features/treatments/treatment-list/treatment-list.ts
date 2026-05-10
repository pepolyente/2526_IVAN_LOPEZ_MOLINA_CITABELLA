import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TreatmentService } from '../../../core/services/treatment.service';
import { TreatmentDetailedResponse, TreatmentRequest } from '../../../shared/models/treatment.model';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-treatment-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Tratamientos</h2>
      <button class="btn-primary" (click)="router.navigate(['/panel/treatments/new'])">+ Nuevo</button>
    </div>

    <div class="filters-bar">
      <div class="filter-group">
        <label>Estado</label>
        <select [(ngModel)]="filterActive" (change)="applyFilter()">
          <option [ngValue]="undefined">Todos</option>
          <option [ngValue]="true">Activos</option>
          <option [ngValue]="false">Inactivos</option>
        </select>
      </div>
      <span class="total-hint">{{ totalElements }} tratamiento(s)</span>
    </div>

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
    } @else if (treatments.length === 0) {
      <p class="empty-state">No hay tratamientos.</p>
    } @else {
      <table class="simple-table">
        <thead>
        <tr>
          <th>Nombre</th>
          <th>Duración mín.</th>
          <th>Precio</th>
          <th>Activo</th>
          <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
          @for (t of treatments; track t.id) {
            <tr [class.row-inactive]="!t.active">
              @if (editingId === t.id) {
                <td><input [(ngModel)]="editForm.name" class="inline-input" /></td>
                <td><input type="number" [(ngModel)]="editForm.minimumDuration" class="inline-input" /></td>
                <td><input type="number" step="0.01" [(ngModel)]="editForm.price" class="inline-input" /></td>
                <td><span class="badge" [class.badge-confirmed]="t.active" [class.badge-cancelled]="!t.active">{{ t.active ? 'Activo' : 'Inactivo' }}</span></td>
                <td class="actions-cell">
                  <button class="btn-xs btn-success" (click)="saveEdit(t.id)">✓</button>
                  <button class="btn-xs btn-outline" (click)="cancelEdit()">✕</button>
                </td>
              } @else {
                <td><strong>{{ t.name }}</strong></td>
                <td>{{ t.minimumDuration }} min</td>
                <td>{{ t.price | currency:'EUR' }}</td>
                <td>
                  <span class="badge" [class.badge-confirmed]="t.active" [class.badge-cancelled]="!t.active">
                    {{ t.active ? 'Activo' : 'Inactivo' }}
                  </span>
                </td>
                <td class="actions-cell">
                  <button class="btn-xs btn-outline" (click)="startEdit(t)">Editar</button>
                  @if (t.active) {
                    <button class="btn-xs btn-danger" (click)="deactivate(t.id)">Desactivar</button>
                  }
                </td>
              }
            </tr>
          }
        </tbody>
      </table>

      <div class="paginator">
        <button class="btn-outline" (click)="prevPage()" [disabled]="page === 0">← Anterior</button>
        <span class="page-info">Pág. {{ page + 1 }} / {{ totalPages }}</span>
        <button class="btn-outline" (click)="nextPage()" [disabled]="page >= totalPages - 1">Siguiente →</button>
      </div>
    }
  `,
})
export class TreatmentList implements OnInit {
  treatments: TreatmentDetailedResponse[] = [];
  loading = true;
  page = 0;
  size = 20;
  totalPages = 0;
  totalElements = 0;
  filterActive: boolean | undefined = undefined;
  editingId: number | null = null;
  editForm: TreatmentRequest = { name: '', minimumDuration: 0, price: 0 };

  constructor(
    private svc: TreatmentService,
    public router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
    private confirmSvc: ConfirmService
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.svc.getDetailed({ page: this.page, size: this.size, active: this.filterActive }).subscribe({
      next: p => {
        this.treatments = p.content;
        this.totalPages = p.totalPages;
        this.totalElements = p.totalElements;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => { this.loading = false; this.cdr.detectChanges(); },
    });
  }

  applyFilter(): void { this.page = 0; this.load(); }
  prevPage(): void { if (this.page > 0) { this.page--; this.load(); } }
  nextPage(): void { if (this.page < this.totalPages - 1) { this.page++; this.load(); } }

  async deactivate(id: number): Promise<void> {
    const ok = await this.confirmSvc.confirm('¿Desactivar este tratamiento?');
    if (!ok) return;
    this.svc.deactivate(id).subscribe({
      next: () => { this.toast.show('Tratamiento desactivado'); this.load(); },
      error: () => this.toast.show('Error', 'error'),
    });
  }

  startEdit(t: TreatmentDetailedResponse): void {
    this.editingId = t.id;
    this.editForm = {
      name: t.name,
      minimumDuration: t.minimumDuration,
      price: t.price,
      description: t.description,
      maximumDuration: t.maximumDuration,
    };
  }

  cancelEdit(): void { this.editingId = null; }

  saveEdit(id: number): void {
    this.svc.update(id, this.editForm).subscribe({
      next: () => {
        this.toast.show('Tratamiento actualizado');
        this.cancelEdit();
        this.load();
      },
      error: err => this.toast.show(err.error?.message ?? 'Error', 'error'),
    });
  }
}
