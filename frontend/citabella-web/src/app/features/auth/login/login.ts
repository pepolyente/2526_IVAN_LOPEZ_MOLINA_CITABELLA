import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  form = {
    username: '',
    password: '',
  };

  showPassword = false;
  loading = false;

  constructor(
    private auth: AuthService,
    private router: Router,
    private toast: ToastService
  ) {}

  submit(): void {
    if (!this.form.username || !this.form.password) {
      this.toast.show('Por favor, completa todos los campos', 'error');
      return;
    }

    this.loading = true;

    this.auth.login({
      username: this.form.username,
      password: this.form.password
    }).subscribe({
      next: (response) => {
        this.toast.show(`Bienvenido, ${response.username}`, 'success');
        setTimeout(() => {
          const role = this.auth.getRole();
          if (role === 'ADMIN' || role === 'EMPLOYEE') {
            this.router.navigate(['/panel/appointments']);
          } else if (role === 'CLIENT') {
            this.router.navigate(['/panel/my-appointments']);
          } else {
            this.router.navigate(['/panel/appointments']);
          }
        }, 1000);
      },
      error: (err) => {
        this.toast.show(err.error?.message ?? 'Usuario o contraseña incorrectos', 'error');
        this.loading = false;
      },
    });
  }
}
