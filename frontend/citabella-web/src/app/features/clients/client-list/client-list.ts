import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ClientService }  from '../../../core/services/client.service';
import { ClientResponse } from '../../../shared/models/client.model';

@Component({
  selector: 'app-client-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Clientes</h2>
      <button class="btn-primary" (click)="router.navigate(['/panel/clients/new'])">+ Nuevo cliente</button>
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
      <span class="total-hint">{{ totalElements }} cliente(s)</span>
    </div>

    @if (loading) {
      <p class="empty-state">Cargando...</p>
    } @else if (clients.length === 0) {
      <p class="empty-state">No hay clientes con los filtros aplicados.</p>
    } @else {
      <table class="simple-table">
        <thead>
        <tr>
          <th>Nombre</th>
          <th>Teléfono</th>
          <th>Género</th>
          <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
          @for (client of clients; track client.id) {
            <tr>
              <td>{{ client.name }}</td>
              <td>{{ client.phoneNumber }}</td>
              <td>{{ client.gender ?? '–' }}</td>
              <td class="actions-cell">
                <button class="btn-xs btn-danger" (click)="deactivate(client.id)" title="Desactivar">
                  Desactivar
                </button>
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
  `
})
export class ClientList implements OnInit {
  clients:        ClientResponse[] = [];
  loading         = true;
  page            = 0;
  size            = 20;
  totalPages      = 0;
  totalElements   = 0;
  filterActive: boolean | undefined = undefined;

  constructor(
    private svc: ClientService,
    public router: Router,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    // Llamada correcta con objeto de parámetros
    this.svc.getAll({ page: this.page, size: this.size, active: this.filterActive }).subscribe({
      next: p => {
        this.clients       = p.content;
        this.totalPages    = p.totalPages;
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

  deactivate(id: number): void {
    if (!confirm('¿Desactivar este cliente?')) return;
    this.svc.deactivate(id).subscribe({ next: () => this.load() });
  }
}
