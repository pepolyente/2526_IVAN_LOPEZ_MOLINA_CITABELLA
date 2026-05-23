import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../../core/services/user.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-user-form',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Nuevo usuario</h2>
      <button class="btn-outline" (click)="router.navigate(['/panel/admin/users'])">Cancelar</button>
    </div>
    <form class="form-card" (ngSubmit)="submit()">
      <div class="form-row">
        <div class="form-group">
          <label>Usuario *</label>
          <input type="text" [(ngModel)]="form.username" name="username" required minlength="4" />
        </div>
        <div class="form-group">
          <label>Email *</label>
          <input type="email" [(ngModel)]="form.email" name="email" required />
        </div>
      </div>
      <div class="form-group">
        <label>Contraseña *</label>
        <input type="password" [(ngModel)]="form.password" name="password" required minlength="6" />
      </div>
      @if (error) { <p class="error-msg">{{ error }}</p> }
      <button type="submit" class="btn-primary" [disabled]="loading">
        {{ loading ? 'Creando...' : 'Crear usuario' }}
      </button>
    </form>
  `
})
export class UserForm {
  form = { username: '', email: '', password: '' };
  loading = false;
  error = '';

  constructor(private svc: UserService, public router: Router, private toast: ToastService) {}

  submit(): void {
    this.loading = true;
    this.svc.create(this.form).subscribe({
      next: () => {
        this.toast.show('Usuario creado correctamente');
        this.router.navigate(['/panel/admin/users']);
      },
      error: err => {
        this.error = err.error?.message ?? 'Error al crear usuario';
        this.loading = false;
      }
    });
  }
}
