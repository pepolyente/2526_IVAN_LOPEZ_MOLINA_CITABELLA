import { Component, HostListener } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  isMenuOpen = false;
  isMobile = false;

  constructor(public auth: AuthService, private router: Router) {
    this.checkScreen();
  }

  @HostListener('window:resize')
  checkScreen() {
    this.isMobile = window.innerWidth < 768;
    if (!this.isMobile) this.isMenuOpen = false;
  }

  toggleMenu() { this.isMenuOpen = !this.isMenuOpen; }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}
