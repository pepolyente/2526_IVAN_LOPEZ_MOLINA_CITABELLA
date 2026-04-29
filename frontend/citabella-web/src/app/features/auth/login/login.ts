import {ChangeDetectorRef, Component} from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { LoginRequest } from '../../../shared/models/auth.model';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  data: LoginRequest = {
    username: '',
    password: '',
  };

  loading = false;
  error   = '';

  constructor(
    private auth: AuthService,
    private router: Router,
    private changeDetectorRef: ChangeDetectorRef) {}

  submit(): void {
    this.loading = true;
    this.error   = '';
    this.auth.login(this.data).subscribe({
      next: () => {
        this.router.navigate(['/panel/appointments']);
      },
      error: () => {
        this.error   = 'Usuario o contraseña incorrectos';
        alert(this.error);
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
    });
  }
}
