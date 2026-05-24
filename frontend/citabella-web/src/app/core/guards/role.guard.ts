import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {

  constructor(private auth: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const allowed: string[] = route.data['roles'] ?? [];
    if (allowed.length === 0 || allowed.includes(this.auth.getRole())) {
      return true;
    }
    this.router.navigate(['/unauthorized']);
    return false;
  }
}
