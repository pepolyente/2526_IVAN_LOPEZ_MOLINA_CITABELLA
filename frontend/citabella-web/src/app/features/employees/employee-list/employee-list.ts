import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { EmployeeService } from '../../../core/services/employee.service';
import { EmployeeResponse } from '../../../shared/models/employee.model';

@Component({
  selector: 'app-employee-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Empleados</h2>
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
          <th>Activo</th>
        </tr>
        </thead>
        <tbody>
          @for (employee of employees; track employee.id) {
            <tr>
              <td>{{ employee.name }}</td>
              <td>{{ employee.position }}</td>
              <td>{{ employee.active ? '✅' : '❌' }}</td>
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

  constructor(private svc: EmployeeService, private changeDetectorRef: ChangeDetectorRef) {}

  ngOnInit(): void {
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
}
