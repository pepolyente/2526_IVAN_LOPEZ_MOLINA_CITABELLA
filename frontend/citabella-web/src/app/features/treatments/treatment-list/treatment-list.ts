import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TreatmentService } from '../../../core/services/treatment.service';
import { TreatmentDetailedResponse } from '../../../shared/models/treatment.model';

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
      <p class="empty-state">Cargando...</p>
    } @else if (treatments.length === 0) {
      <p class="empty-state">No hay tratamientos.</p>
    } @else {
      <table class="simple-table">
        <thead>
        <tr>
          <th>Nombre</th>
          <th>Descripción</th>
          <th>Min. dur.</th>
          <th>Máx. dur.</th>
          <th>Precio</th>
          <th>Activo</th>
          <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
          @for (t of treatments; track t.id) {
            <tr [class.row-inactive]="!t.active">
              <td><strong>{{ t.name }}</strong></td>
              <td class="desc-cell">{{ t.description ?? '–' }}</td>
              <td>{{ t.minimumDuration }} min</td>
              <td>{{ t.maximumDuration ? t.maximumDuration + ' min' : '–' }}</td>
              <td>{{ t.price | currency:'EUR' }}</td>
              <td>
                <span class="badge" [class.badge-confirmed]="t.active" [class.badge-cancelled]="!t.active">
                  {{ t.active ? 'Activo' : 'Inactivo' }}
                </span>
              </td>
              <td class="actions-cell">
                @if (t.active) {
                  <button class="btn-xs btn-danger" (click)="deactivate(t.id)">Desactivar</button>
                }
              </td>
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
  treatments:   TreatmentDetailedResponse[] = [];
  loading       = true;
  page          = 0;
  size          = 20;
  totalPages    = 0;
  totalElements = 0;
  filterActive: boolean | undefined = undefined;

  constructor(
    private svc: TreatmentService,
    public router: Router,
    private changeDetectorRef: ChangeDetectorRef,
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.svc.getDetailed({ page: this.page, size: this.size, active: this.filterActive }).subscribe({
      next: p => {
        this.treatments    = p.content;
        this.totalPages    = p.totalPages;
        this.totalElements = p.totalElements;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => { this.loading = false; this.changeDetectorRef.detectChanges(); },
    });
  }

  applyFilter(): void { this.page = 0; this.load(); }
  prevPage(): void { if (this.page > 0) { this.page--; this.load(); } }
  nextPage(): void { if (this.page < this.totalPages - 1) { this.page++; this.load(); } }

  deactivate(id: number): void {
    if (!confirm('¿Desactivar este tratamiento?')) return;
    this.svc.deactivate(id).subscribe({ next: () => this.load() });
  }
}
