import { Component, HostListener, OnInit } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { ThemeService } from '../../core/services/theme.service';

@Component({
  selector: 'app-public-header',
  standalone: false,
  templateUrl: './public-header.html',
  styleUrl: './public-header.css',
})
export class PublicHeader implements OnInit {
  isMenuOpen = false;
  isMobile   = false;
  isScrolled = false;

  constructor(
    public auth: AuthService,
    private router: Router,
    public theme: ThemeService,
  ) {
    this.checkScreen();
  }

  ngOnInit(): void {
    this.onScroll();
  }

  @HostListener('window:resize')
  checkScreen() {
    this.isMobile = window.innerWidth < 768;
    if (!this.isMobile) this.isMenuOpen = false;
  }

  @HostListener('window:scroll')
  onScroll() {
    this.isScrolled = window.scrollY > 10;
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  /**
   * Redirige al panel correspondiente al rol del usuario.
   * Si el usuario no tiene un rol reconocido con acceso al panel,
   * no realiza ninguna navegación (el botón ya no se mostrará en la plantilla,
   * pero esta comprobación actúa como segunda línea de defensa).
   */
  goToPanel(): void {
    const panelRoute = this.auth.getPanelRoute();
    if (panelRoute) {
      this.router.navigate([panelRoute]);
    }
    this.isMenuOpen = false;
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
    this.isMenuOpen = false;
  }
}
