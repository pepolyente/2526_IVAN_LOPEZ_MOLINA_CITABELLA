import { Component, HostListener } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { ThemeService } from '../../core/services/theme.service';

interface NavItem {
  path: string;
  label: string;
  icon: string;
  roles: string[];
}

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  readonly navItems: NavItem[] = [
    { path: '/panel/appointments',    label: 'Agenda',    icon: 'calendar_month', roles: ['ADMIN', 'EMPLOYEE'] },
    { path: '/panel/my-appointments', label: 'Mis citas', icon: 'event_available', roles: ['CLIENT'] },
    { path: '/panel/clients',         label: 'Clientes',  icon: 'people',          roles: ['ADMIN', 'EMPLOYEE'] },
    { path: '/panel/products',        label: 'Productos', icon: 'inventory_2',     roles: ['ADMIN', 'EMPLOYEE'] },
    { path: '/panel/employees',       label: 'Equipo',    icon: 'badge',           roles: ['ADMIN'] },
    { path: '/panel/treatments',      label: 'Servicios', icon: 'spa',             roles: ['ADMIN'] },
    { path: '/panel/admin/users',     label: 'Usuarios',  icon: 'manage_accounts', roles: ['ADMIN'] },
  ];

  isMenuOpen = false;
  isMobile = false;

  constructor(public auth: AuthService, private router: Router,public theme: ThemeService, ) {
    this.checkScreen();
  }

  get visibleNavItems(): NavItem[] {
    return this.navItems.filter(item =>
      item.roles.length === 0 || item.roles.includes(this.auth.getRole())
    );
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
