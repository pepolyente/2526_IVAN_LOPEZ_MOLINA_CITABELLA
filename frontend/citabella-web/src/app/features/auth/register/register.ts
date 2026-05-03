import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
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
  error = '';
  success = '';

  constructor(private auth: AuthService, private router: Router) {}

  submit(): void {
    if (!this.data.username || !this.data.email || !this.data.password) {
      this.error = 'Todos los campos son obligatorios';
      return;
    }

    if (this.data.password.length < 6) {
      this.error = 'La contraseña debe tener al menos 6 caracteres';
      return;
    }

    if (!this.data.email.includes('@') || !this.data.email.includes('.')) {
      this.error = 'El email no es válido';
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

    this.auth.register(this.data).subscribe({
      next: () => {
        this.success = 'Cuenta creada exitosamente. Redirigiendo...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1500);
      },
      error: err => {
        this.error = err.error?.message ?? 'Error al registrarse';
        this.loading = false;
      },
    });
  }
}
