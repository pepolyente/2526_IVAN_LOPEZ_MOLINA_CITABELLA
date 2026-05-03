import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainLayout } from './layout/main-layout/main-layout';
import { PublicLayout } from './layout/public-layout/public-layout';
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import {Register} from './features/auth/register/register';
import {Login} from './features/auth/login/login';

const routes: Routes = [

  // ─── PUBLIC ZONE ────────────────────────────────────────────────────────
  {
    path: '',
    component: PublicLayout,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./features/public/public-module').then(m => m.PublicModule),
      },
    ],
  },

  // ─── AUTHENTICATION ───────────────────────────────────────────────────────
  {
    path: 'login',
    component: Login,
  },
  {
    path: 'register',
    component: Register,
  },

  // ─── PRIVATE PANEL ───────────────────────────────────────────────────────
  {
    path: 'panel',
    component: MainLayout,
    canActivate: [AuthGuard],
    children: [

      // ─── ADMIN + EMPLOYEE ───────────────────────────────────────────────────────
      {
        path: 'appointments',
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN', 'EMPLOYEE'] },
        loadChildren: () =>
          import('./features/appointments/appointments-module')
            .then(m => m.AppointmentsModule),
      },
      {
        path: 'clients',
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN', 'EMPLOYEE'] },
        loadChildren: () =>
          import('./features/clients/clients-module')
            .then(m => m.ClientsModule),
      },

      // ─── ADMIN ───────────────────────────────────────────────────────
      {
        path: 'employees',
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN'] },
        loadChildren: () =>
          import('./features/employees/employees-module')
            .then(m => m.EmployeesModule),
      },
      {
        path: 'treatments',
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN'] },
        loadChildren: () =>
          import('./features/treatments/treatments-module')
            .then(m => m.TreatmentsModule),
      },
      {
        path: 'products',
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN', 'EMPLOYEE'] },
        loadChildren: () =>
          import('./features/product/product-module')
            .then(m => m.ProductModule),
      },
      {
        path: 'admin',
        canActivate: [RoleGuard],
        data: { roles: ['ADMIN'] },
        loadChildren: () =>
          import('./features/admin/admin-module').then(m => m.AdminModule),
      },

      // ──── CLIENT ───────────────────────────────────────────────────────
      {
        path: 'my-appointments',
        canActivate: [RoleGuard],
        data: { roles: ['CLIENT'] },
        loadChildren: () =>
          import('./features/appointments/appointments-module')
            .then(m => m.AppointmentsModule),
      },

      { path: '', redirectTo: 'appointments', pathMatch: 'full' },
    ],
  },

  // ─── UNAUTHORIZED ────────────────────────────────────────────────────────
  {
    path: 'unauthorized',
    redirectTo: '/',
  },

  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
