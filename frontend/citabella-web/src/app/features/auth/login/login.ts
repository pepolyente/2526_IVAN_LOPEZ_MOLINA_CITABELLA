import { ChangeDetectorRef, Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

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
  error   = '';

  constructor(
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  submit(): void {
    console.log('form value:', this.form);
    if (!this.form.username || !this.form.password) {
      this.error = 'Por favor, completa todos los campos';
      this.cdr.detectChanges();
      return;
    }

    this.loading = true;
    this.error   = '';
    this.cdr.detectChanges();

    this.auth.login({
      username: this.form.username,
      password: this.form.password
    }).subscribe({
      next: (response) => {
        console.log('Login exitoso:', response);
        setTimeout(() => {
          const role = this.auth.getRole();
          console.log('Rol detectado:', role);
          if (role === 'ADMIN' || role === 'EMPLOYEE') {
            this.router.navigate(['/panel/appointments']);
          } else if (role === 'CLIENT') {
            this.router.navigate(['/panel/my-appointments']);
          } else {
            this.router.navigate(['/panel/appointments']);
          }
        }, 100);
      },
      error: (err) => {
        console.error('Error en login:', err);
        this.error = 'Usuario o contraseña incorrectos';
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }
}
