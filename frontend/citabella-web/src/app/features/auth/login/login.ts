import { ChangeDetectorRef, Component } from '@angular/core';
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
  form = { username: '', password: '' };
  showPassword = false;
  loading = false;
  error = '';

  constructor(
    private auth: AuthService,
    private router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef
  ) {}

  submit(): void {
    if (!this.form.username || !this.form.password) {
      this.toast.show('Por favor, completa todos los campos', 'error');
      return;
    }
    this.loading = true;
    this.error = '';

    this.auth.login({ username: this.form.username, password: this.form.password })
      .subscribe({
        next: (response) => {
          this.loading = false;  // ← deshabilitar carga
          this.toast.show(`Bienvenido, ${response.username}`, 'success');
          setTimeout(() => {
            const panelRoute = this.auth.getPanelRoute();
            if (panelRoute) this.router.navigate([panelRoute]);
            else this.router.navigate(['/']);
          }, 1000);
        },
        error: (err) => {
          this.loading = false;
          let msg = 'Usuario o contraseña incorrectos';
          if (err.error?.message) msg = err.error.message;
          else if (typeof err.error === 'string') msg = err.error;
          this.error = msg;
          this.toast.show(msg, 'error');
          this.cdr.detectChanges();
        }
      });
  }
}
