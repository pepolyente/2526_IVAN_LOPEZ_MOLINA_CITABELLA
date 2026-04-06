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

  constructor(private auth: AuthService, private router: Router) {}

  submit(): void {
    this.loading = true;
    this.error = '';
    this.auth.register(this.data).subscribe({
      next: () => this.router.navigate(['/login']),
      error: err => {
        this.error = err.error?.message ?? 'Error al registrar';
        this.loading = false;
      },
    });
  }
}
