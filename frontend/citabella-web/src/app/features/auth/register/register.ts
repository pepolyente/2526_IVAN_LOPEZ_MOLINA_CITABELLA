import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { RegisterRequest } from '../../../shared/models/auth.model';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  data: RegisterRequest = { username: '', password: '', email: '' };
  loading = false;

  constructor(
    private auth: AuthService,
    private router: Router,
    private toast: ToastService
  ) {}

  submit(): void {
    if (!this.data.username || !this.data.email || !this.data.password) {
      this.toast.show('Todos los campos son obligatorios', 'warning');
      return;
    }

    if (this.data.password.length < 6) {
      this.toast.show('La contraseña debe tener al menos 6 caracteres', 'warning');
      return;
    }

    if (!this.data.email.includes('@') || !this.data.email.includes('.')) {
      this.toast.show('El email no es válido', 'warning');
      return;
    }

    this.loading = true;

    this.auth.register(this.data).subscribe({
      next: () => {
        this.toast.show('Cuenta creada correctamente', 'success');
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1500);
      },
      error: err => {
        this.toast.show(err.error?.message ?? 'Error al registrarse', 'error');
        this.loading = false;
      },
    });
  }
}
