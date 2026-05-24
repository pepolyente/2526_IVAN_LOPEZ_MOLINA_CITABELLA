import {ChangeDetectorRef, Component} from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { RegisterRequest } from '../../../shared/models/auth.model';
import { finalize } from 'rxjs/operators';

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

  constructor(private auth: AuthService, private router: Router, private toast: ToastService,
              private changeDetectorRef: ChangeDetectorRef) {}

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
    this.error = '';

    this.auth.register(this.data)
      .pipe(finalize(() => { this.loading = false; }))
      .subscribe({
        next: () => {
          this.toast.show('Cuenta creada correctamente', 'success');
          setTimeout(() => this.router.navigate(['/login']), 1500);
        },
        error: (err) => {
          let msg = 'Error al registrarse';
          if (err.error?.message) msg = err.error.message;
          else if (typeof err.error === 'string') msg = err.error;
          else if (err.message) msg = err.message;

          this.error = msg;
          this.toast.show(msg, 'error');
          this.changeDetectorRef.detectChanges();
        }
      });
  }
}
