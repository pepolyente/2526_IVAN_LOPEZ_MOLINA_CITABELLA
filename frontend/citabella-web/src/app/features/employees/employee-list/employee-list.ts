import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../../../core/services/employee.service';
import { EmployeeResponse } from '../../../shared/models/employee.model';

@Component({
  selector: 'app-employee-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Empleados</h2>
    </div>
    <table class="simple-table">
      <thead><tr><th>Nombre</th><th>Puesto</th><th>Activo</th></tr></thead>
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
  `,
})
export class EmployeeList implements OnInit {
  employees: EmployeeResponse[] = [];
  constructor(private svc: EmployeeService) {}
  ngOnInit(): void { this.svc.getAll().subscribe(data => (this.employees = data)); }
}
