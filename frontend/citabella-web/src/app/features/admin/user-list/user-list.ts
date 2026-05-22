import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UserService } from '../../../core/services/user.service';
import { AccountStatus, UserResponse } from '../../../shared/models/user.model';
import { ToastService } from '../../../core/services/toast.service';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-user-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Usuarios del sistema</h2>
      <button class="btn-primary" (click)="showCreateForm = !showCreateForm">
        {{ showCreateForm ? 'Cancelar' : '+ Nuevo usuario' }}
      </button>
    </div>

    @if (showCreateForm) {
      <form class="form-card create-form" (ngSubmit)="createUser()">
        <h3>Nuevo usuario</h3>
        <div class="form-row">
          <div class="form-group">
            <label>Usuario *</label>
            <input type="text" [(ngModel)]="newUser.username" name="username" required minlength="4" />
          </div>
          <div class="form-group">
            <label>Email *</label>
            <input type="email" [(ngModel)]="newUser.email" name="email" required />
          </div>
        </div>
        <div class="form-group">
          <label>Contraseña *</label>
          <input type="password" [(ngModel)]="newUser.password" name="password" required minlength="6" />
        </div>
        @if (createError) { <p class="error-msg">{{ createError }}</p> }
        <button type="submit" class="btn-primary" [disabled]="creating">
          {{ creating ? 'Creando...' : 'Crear usuario' }}
        </button>
      </form>
    }

    <div class="filters-bar">
      <div class="filter-group">
        <label>Estado de cuenta</label>
        <select [(ngModel)]="filterStatus" (change)="applyFilter()">
          <option value="">Todos</option>
          <option value="PENDING">Pendiente</option>
          <option value="ACTIVE">Activo</option>
          <option value="LOCKED">Bloqueado</option>
        </select>
      </div>

      <div class="filter-group search-group">
        <label>Buscar usuario</label>
        <div class="search-wrapper">
          <input type="text"
                 [(ngModel)]="searchTerm"
                 placeholder="Usuario o email..."
                 (keyup.enter)="onSearch()"
                 class="inline-input" />
          <button class="btn-outline" (click)="onSearch()">Buscar</button>
          @if (searchTerm) {
            <button class="btn-outline" (click)="clearSearch()">✕</button>
          }
        </div>
      </div>


    </div>
    <span class="total-hint">{{ totalElements }} usuario(s)</span>
    @if (loading) {
      <div class="skeleton-table">
        @for (i of [1,2,3,4,5]; track i) {
          <div class="skeleton-row">
            <div class="skeleton-cell sk-wide"></div>
            <div class="skeleton-cell sk-medium"></div>
            <div class="skeleton-cell sk-narrow"></div>
          </div>
        }
      </div>
    } @else if (users.length === 0) {
      <p class="empty-state">No hay usuarios con los filtros aplicados.</p>
    } @else {
      <div class="table-wrapper">
        <table class="simple-table">
          <thead>
          <tr>
            <th>Usuario</th>
            <th>Email</th>
            <th>Rol</th>
            <th>Estado cuenta</th>
            <th>Perfil</th>
            <th>Acciones</th>
          </tr>
          </thead>
          <tbody>
            @for (u of users; track u.id) {
              <tr>
                <td data-label="Usuario">{{ u.username }}</td>
                <td data-label="Email">{{ u.email }}</td>
                <td data-label="Rol"><span class="badge">{{ u.role }}</span></td>
                <td data-label="Estado cuenta">
                <span class="badge"
                      [class.badge-confirmed]="u.accountStatus === 'ACTIVE'"
                      [class.badge-pending]="u.accountStatus === 'PENDING'"
                      [class.badge-cancelled]="u.accountStatus === 'LOCKED'">
                  {{ u.accountStatus }}
                </span>
                </td>
                <td data-label="Perfil">
                <span class="badge"
                      [class.badge-in_progress]="u.profileType === 'EMPLOYEE'"
                      [class.badge-completed]="u.profileType === 'CLIENT'">
                  {{ u.profileType }}
                </span>
                </td>
                <td data-label="Acciones" class="actions-cell">
                  @if (swapRoleId === u.id) {
                    <div class="inline-action">
                      <select [(ngModel)]="swapRoleName">
                        <option value="">-- rol --</option>
                        <option value="ADMIN">ADMIN</option>
                        <option value="EMPLOYEE">EMPLOYEE</option>
                        <option value="CLIENT">CLIENT</option>
                        <option value="USER">USER</option>
                      </select>
                      <button class="btn-xs btn-success" (click)="confirmSwapRole(u.id)" [disabled]="!swapRoleName">✓</button>
                      <button class="btn-xs btn-outline" (click)="cancelSwapRole()">✕</button>
                    </div>
                  } @else {
                    <button class="btn-xs btn-outline" (click)="startSwapRole(u.id)">Rol</button>
                  }
                  @if (u.accountStatus !== 'LOCKED') {
                    <button class="btn-xs btn-danger" (click)="deactivate(u.id)"><span class="material-symbols-outlined">person_off</span></button>
                  }
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>

      <div class="paginator">
        <button class="btn-outline" (click)="prevPage()" [disabled]="page === 0">← Anterior</button>
        <span class="page-info">Pág. {{ page + 1 }} / {{ totalPages }}</span>
        <button class="btn-outline" (click)="nextPage()" [disabled]="page >= totalPages - 1">Siguiente →</button>
      </div>
    }
  `,
})
export class UserList implements OnInit {
  users:         UserResponse[] = [];
  loading        = true;
  page           = 0;
  size           = 20;
  totalPages     = 0;
  totalElements  = 0;
  filterStatus: AccountStatus | '' = '';
  searchTerm = '';

  showCreateForm = false;
  newUser = { username: '', email: '', password: '' };
  creating    = false;
  createError = '';

  swapRoleId: number | null = null;
  swapRoleName = '';

  constructor(
    private svc: UserService,
    private changeDetectorRef: ChangeDetectorRef,
    private toast: ToastService,
    private confirmSvc: ConfirmService
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    const accountStatus = this.filterStatus || undefined;
    this.svc.getAll({ page: this.page, size: this.size, accountStatus, search: this.searchTerm || undefined  }).subscribe({
      next: p => {
        this.users         = p.content;
        this.totalPages    = p.totalPages;
        this.totalElements = p.totalElements;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => { this.loading = false; this.changeDetectorRef.detectChanges(); },
    });
  }

  onSearch(): void {
    this.page = 0;
    this.load();
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.onSearch();
  }
  applyFilter(): void { this.page = 0; this.load(); }
  prevPage(): void { if (this.page > 0) { this.page--; this.load(); } }
  nextPage(): void { if (this.page < this.totalPages - 1) { this.page++; this.load(); } }

  createUser(): void {
    this.creating    = true;
    this.createError = '';
    this.svc.create(this.newUser).subscribe({
      next: () => {
        this.creating      = false;
        this.showCreateForm = false;
        this.newUser = { username: '', email: '', password: '' };
        this.load();
      },
      error: err => {
        this.createError = err.error?.message ?? 'Error al crear el usuario';
        this.creating    = false;
        this.changeDetectorRef.detectChanges();
      },
    });
  }

  async deactivate(id: number): Promise<void> {
    const ok = await this.confirmSvc.confirm('¿Bloquear este usuario?');
    if (!ok) return;
    this.svc.deactivate(id).subscribe({
      next: () => { this.toast.show('Usuario bloqueado correctamente'); this.load(); }
    });
  }

  startSwapRole(id: number): void  { this.swapRoleId = id; this.swapRoleName = ''; }
  cancelSwapRole(): void           { this.swapRoleId = null; this.swapRoleName = ''; }

  confirmSwapRole(id: number): void {
    if (!this.swapRoleName) return;
    this.svc.swapRole(id, this.swapRoleName).subscribe({
      next: () => { this.toast.show('Rol cambiado correctamente'); this.cancelSwapRole(); this.load(); },
      error: () => { this.toast.show('Error al cambiar el rol', 'error'); },
    });
  }
}
