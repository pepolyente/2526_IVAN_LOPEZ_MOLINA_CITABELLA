import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { EmployeeService } from '../../../core/services/employee.service';
import { EmployeeResponse, EmployeeRequest } from '../../../shared/models/employee.model';
import { Router } from '@angular/router';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-employee-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Empleados</h2>
      <button class="btn-primary" (click)="router.navigate(['/panel/employees/new'])">
        <span class="material-symbols-outlined">add</span>
      </button>
    </div>
    <div class="filters-bar">
      <div class="filter-group search-group">
        <label>Buscar empleado</label>
        <div class="search-wrapper">
          <input type="text"
                 [(ngModel)]="searchTerm"
                 placeholder="Nombre o puesto..."
                 (keyup.enter)="onSearch()"
                 class="inline-input" />
          <button class="btn-outline" (click)="onSearch()">Buscar</button>
          @if (searchTerm) {
            <button class="btn-outline" (click)="clearSearch()">✕</button>
          }
        </div>
      </div>

    </div>
    <span class="total-hint">{{ employees.length }} empleado(s)</span>
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
    } @else if (employees.length === 0) {
      <p class="empty-state">No hay empleados registrados.</p>
    } @else {
      <div class="table-wrapper">
        <table class="simple-table">
          <thead>
          <tr>
            <th>Nombre</th>
            <th>Puesto</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
          </thead>
          <tbody>
            @for (employee of employees; track employee.id) {
              <tr [class.row-inactive]="!employee.active">
                @if (editingId === employee.id) {
                  <td data-label="Nombre"><input [(ngModel)]="editForm.name" class="inline-input" /></td>
                  <td data-label="Puesto"><input [(ngModel)]="editForm.position" class="inline-input" /></td>
                  <td data-label="Estado">
                  <span class="badge" [class.badge-confirmed]="employee.active" [class.badge-cancelled]="!employee.active">
                    {{ employee.active ? 'Activo' : 'Inactivo' }}
                  </span>
                  </td>
                  <td data-label="Acciones" class="actions-cell">
                    <button class="btn-xs btn-success" (click)="saveEdit(employee.id)">✓</button>
                    <button class="btn-xs btn-outline" (click)="cancelEdit()">✕</button>
                  </td>
                } @else {
                  <td data-label="Nombre">{{ employee.name }}</td>
                  <td data-label="Puesto">{{ employee.position }}</td>
                  <td data-label="Estado">
                  <span class="badge" [class.badge-confirmed]="employee.active" [class.badge-cancelled]="!employee.active">
                    {{ employee.active ? 'Activo' : 'Inactivo' }}
                  </span>
                  </td>
                  <td data-label="Acciones" class="actions-cell">
                    <button class="btn-xs btn-outline" (click)="startEdit(employee)" title="Editar">
                      <span class="material-symbols-outlined">edit</span>
                    </button>
                    @if (employee.active) {
                      <button class="btn-xs btn-danger" (click)="deactivate(employee.id)"><span class="material-symbols-outlined">person_off</span></button>
                    } @else {
                      <button class="btn-xs btn-success" (click)="activate(employee.id)">Activar</button>
                    }
                  </td>
                }
              </tr>
            }
          </tbody>
        </table>
      </div>
    }
  `,
})
export class EmployeeList implements OnInit {
  employees: EmployeeResponse[] = [];
  loading = true;
  editingId: number | null = null;
  editForm: EmployeeRequest = { name: '', position: '' };
  searchTerm = '';

  constructor(
    private svc: EmployeeService,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
    public router: Router,
    private confirmSvc: ConfirmService
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.svc.getAll({ page: 0, size: 200 ,search: this.searchTerm || undefined}).subscribe({
      next: data => {
        this.employees = data.content;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onSearch(): void {
    this.load();
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.onSearch();
  }

  startEdit(employee: EmployeeResponse): void {
    this.editingId = employee.id;
    this.editForm = { name: employee.name, position: employee.position };
  }

  cancelEdit(): void { this.editingId = null; }

  saveEdit(id: number): void {
    this.svc.update(id, this.editForm).subscribe({
      next: updated => {
        const idx = this.employees.findIndex(e => e.id === id);
        if (idx !== -1) this.employees[idx] = updated;
        this.cancelEdit();
        this.toast.show('Empleado actualizado');
        this.cdr.detectChanges();
      },
      error: err => this.toast.show(err.error?.message ?? 'Error', 'error'),
    });
  }

  async deactivate(id: number): Promise<void> {
    const ok = await this.confirmSvc.confirm('¿Desactivar este empleado?');
    if (!ok) return;
    this.svc.delete(id).subscribe({
      next: () => { this.toast.show('Empleado desactivado'); this.load(); },
      error: () => this.toast.show('Error al desactivar', 'error')
    });
  }

  async activate(id: number): Promise<void> {
    const ok = await this.confirmSvc.confirm('¿Activar este empleado?');
    if (!ok) return;
    this.svc.activate(id).subscribe({
      next: () => { this.toast.show('Empleado activado'); this.load(); },
      error: () => this.toast.show('Error al activar', 'error')
    });
  }
}
