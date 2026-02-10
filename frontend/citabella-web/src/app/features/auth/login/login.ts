import { Component } from '@angular/core';
import { Auth } from '../../../core/services/auth';
import { Router } from '@angular/router';
import { LoginRequest } from '../../../shared/models/login-request.model';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  data: LoginRequest = {
    username: '',
    password: ''
  };

  constructor(
    private auth: Auth,
    private router: Router
  ) {}

  submit(): void {
    this.auth.login(this.data).subscribe({
      next: res => {
        this.auth.saveToken(res.token);
        this.router.navigate(['/appointments']);
      },
      error: () => {
        alert('Credenciales incorrectas');
      }
    });
  }
}
