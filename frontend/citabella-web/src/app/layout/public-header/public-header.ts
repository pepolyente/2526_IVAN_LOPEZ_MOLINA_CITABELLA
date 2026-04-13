import { Component } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import {ThemeService} from '../../core/services/theme.service';

@Component({
  selector: 'app-public-header',
  standalone: false,
  templateUrl: './public-header.html',
  styleUrl: './public-header.css',
})
export class PublicHeader {
  constructor(public auth: AuthService, private router: Router,public theme: ThemeService) {}

  goToPanel(): void {
    const role = this.auth.getRole();
    if (role === 'ADMIN' || role === 'EMPLOYEE') {
      this.router.navigate(['/panel/appointments']);
    } else if (role === 'CLIENT') {
      this.router.navigate(['/panel/my-appointments']);
    }
  }
}
