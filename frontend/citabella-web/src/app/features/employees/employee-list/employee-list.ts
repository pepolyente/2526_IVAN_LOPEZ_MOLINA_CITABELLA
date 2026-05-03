import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { EmployeeService } from '../../../core/services/employee.service';
import { EmployeeResponse } from '../../../shared/models/employee.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-employee-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Empleados</h2>
      <button class="btn-primary" (click)="router.navigate(['/panel/employees/new'])">+ Nuevo empleado</button>
    </div>
    @if (loading) {
      <p class="empty-state">Cargando...</p>
    } @else if (employees.length === 0) {
      <p class="empty-state">No hay empleados registrados.</p>
    } @else {
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
              <td>{{ employee.name }}</td>
              <td>{{ employee.position }}</td>
              <td>
                <span class="badge"
                      [class.badge-confirmed]="employee.active"
                      [class.badge-cancelled]="!employee.active">
                  {{ employee.active ? 'Activo' : 'Inactivo' }}
                </span>
              </td>
              <td class="actions-cell">
                @if (employee.active) {
                  <button class="btn-xs btn-danger" (click)="deactivate(employee.id)">Desactivar</button>
                } @else {
                  <button class="btn-xs btn-success" (click)="activate(employee.id)">Activar</button>
                }
              </td>
            </tr>
          }
        </tbody>
      </table>
    }
  `,
})
export class EmployeeList implements OnInit {
  employees: EmployeeResponse[] = [];
  loading = true;

  constructor(
    private svc: EmployeeService,
    private changeDetectorRef: ChangeDetectorRef,
    public router: Router
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.loading = true;
    this.svc.getAll({ page: 0, size: 200 }).subscribe({
      next: data => {
        this.employees = data.content;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  deactivate(id: number): void {
    if (!confirm('¿Desactivar este empleado?')) return;
    this.svc.delete(id).subscribe({
      next: () => this.loadEmployees(),
      error: () => alert('Error al desactivar el empleado')
    });
  }

  activate(id: number): void {
    if (!confirm('¿Activar este empleado?')) return;
    this.svc.activate(id).subscribe({
      next: () => this.loadEmployees(),
      error: () => alert('Error al activar el empleado')
    });
  }
}
