import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { EmployeeService } from '../../../core/services/employee.service';
import { EmployeeRequest } from '../../../shared/models/employee.model';

@Component({
  selector: 'app-employee-form',
  standalone: false,
  template: `
    <div class="page-header modal-create">
      <h2>Nuevo empleado</h2>
      <button class="btn-outline" (click)="router.navigate(['/panel/employees'])">Cancelar</button>
    </div>
    <form class="form-card" (ngSubmit)="submit()">
      <div class="form-group">
        <label>Nombre *</label>
        <input type="text" [(ngModel)]="form.name" name="name" required />
      </div>
      <div class="form-group">
        <label>Puesto *</label>
        <input type="text" [(ngModel)]="form.position" name="position" required />
      </div>
      @if (error) { <p class="error-msg">{{ error }}</p> }
      <button type="submit" class="btn-primary" [disabled]="loading">
        {{ loading ? 'Guardando...' : 'Crear empleado' }}
      </button>
    </form>
  `,
})
export class EmployeeForm {
  form: EmployeeRequest = { name: '', position: '' };
  loading = false;
  error   = '';

  constructor(private svc: EmployeeService, public router: Router) {}

  submit(): void {
    this.loading = true;
    this.svc.create(this.form).subscribe({
      next: () => this.router.navigate(['/panel/employees']),
      error: err => { this.error = err.error?.message ?? 'Error al crear empleado'; this.loading = false; },
    });
  }
}
