import { Component, HostListener } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { ThemeService } from '../../core/services/theme.service';

@Component({
  selector: 'app-public-header',
  standalone: false,
  templateUrl: './public-header.html',
  styleUrl: './public-header.css',
})
export class PublicHeader {
  isMenuOpen = false;
  isMobile = false;

  constructor(
    public auth: AuthService,
    private router: Router,
    public theme: ThemeService,
  ) {
    this.checkScreen();
  }

  @HostListener('window:resize')
  checkScreen() {
    this.isMobile = window.innerWidth < 768;
    if (!this.isMobile) this.isMenuOpen = false;
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  goToPanel(): void {
    const role = this.auth.getRole();
    if (role === 'ADMIN' || role === 'EMPLOYEE') {
      this.router.navigate(['/panel/appointments']);
    } else if (role === 'CLIENT') {
      this.router.navigate(['/panel/my-appointments']);
    } else {
      this.router.navigate(['/panel/appointments']);
    }
    this.isMenuOpen = false;
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
    this.isMenuOpen = false;
  }
}
