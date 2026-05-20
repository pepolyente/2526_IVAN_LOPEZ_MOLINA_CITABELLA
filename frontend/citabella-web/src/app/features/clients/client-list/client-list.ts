import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ClientService } from '../../../core/services/client.service';
import { UserService } from '../../../core/services/user.service';
import { ToastService } from '../../../core/services/toast.service';
import { ClientRequest, ClientResponse } from '../../../shared/models/client.model';
import { UserResponse } from '../../../shared/models/user.model';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-client-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Clientes</h2>
      <button class="btn-primary" (click)="router.navigate(['/panel/clients/new'])">+ Nuevo cliente</button>
    </div>

    <div class="filters-bar">
      <div class="filter-group">
        <label>Estado</label>
        <select [(ngModel)]="filterActive" (change)="applyFilter()">
          <option [ngValue]="undefined">Todos</option>
          <option [ngValue]="true">Activos</option>
          <option [ngValue]="false">Inactivos</option>
        </select>
      </div>

      <div class="filter-group search-group">
        <label>Buscar</label>
        <div class="search-wrapper">
          <input type="text"
                 [(ngModel)]="searchTerm"
                 placeholder="Nombre o teléfono..."
                 (keyup.enter)="onSearch()"
                 class="inline-input" />
          <button class="btn-outline" (click)="onSearch()">Buscar</button>
          @if (searchTerm) {
            <button class="btn-outline" (click)="clearSearch()">✕</button>
          }
        </div>
      </div>
      <span class="total-hint">{{ totalElements }} cliente(s)</span>
    </div>

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
    } @else if (clients.length === 0) {
      <p class="empty-state">No hay clientes con los filtros aplicados.</p>
    } @else {
      <div class="table-wrapper">
        <table class="simple-table">
          <thead>
          <tr>
            <th>Nombre</th>
            <th>Teléfono</th>
            <th>Género</th>
            <th>Usuario</th>
            <th>Acciones</th>
          </tr>
          </thead>
          <tbody>
            @for (client of clients; track client.id) {
              <tr [class.row-inactive]="!client.active">
                @if (editingId === client.id) {
                  <td><input [(ngModel)]="editForm.name" class="inline-input" /></td>
                  <td><input [(ngModel)]="editForm.phoneNumber" class="inline-input" /></td>
                  <td>
                    <select [(ngModel)]="editForm.gender" class="inline-input">
                      <option value="">—</option>
                      <option value="MALE">Hombre</option>
                      <option value="FEMALE">Mujer</option>
                      <option value="OTHER">Otro</option>
                    </select>
                  </td>
                  <td>{{ client.linkedUsername ?? '—' }}</td>
                  <td class="actions-cell">
                    <button class="btn-xs btn-success" (click)="saveEdit(client.id)">✓</button>
                    <button class="btn-xs btn-outline" (click)="cancelEdit()">✕</button>
                  </td>
                } @else {
                  <td>{{ client.name }}</td>
                  <td>{{ client.phoneNumber }}</td>
                  <td>{{ client.gender ?? '–' }}</td>
                  <td>
                    @if (client.linkedUsername) {
                      <span class="badge badge-confirmed">{{ client.linkedUsername }}</span>
                    } @else {
                      <span class="badge badge-cancelled">Sin cuenta</span>
                    }
                  </td>
                  <td class="actions-cell">
                    <button class="btn-xs btn-outline" (click)="startEdit(client)" title="Editar">
                      <span class="material-symbols-outlined">edit</span>
                    </button>
                    @if (!client.linkedUsername) {
                      <button class="btn-xs btn-outline" (click)="openLinkModal(client)" title="Vincular usuario">
                        <span class="material-symbols-outlined">link</span>
                      </button>
                    } @else {
                      <button class="btn-xs btn-danger" (click)="unlink(client.id)" title="Desvincular usuario">
                        <span class="material-symbols-outlined">link_off</span>
                      </button>
                    }
                    <button class="btn-xs btn-danger" (click)="deactivate(client.id)" title="Desactivar">
                      <span class="material-symbols-outlined">person_off</span>
                    </button>
                  </td>
                }
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

    @if (showLinkModal) {
      <div class="modal-overlay" (click)="showLinkModal = false">
        <div class="modal-card" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h3>Vincular usuario a {{ selectedClient?.name }}</h3>
            <button class="modal-close" (click)="showLinkModal = false">✕</button>
          </div>
          <div class="modal-body">
            <input type="text" [(ngModel)]="userSearchTerm" (input)="searchUsers()" placeholder="Buscar usuario..." class="input-large" />
            @if (userResults.length > 0) {
              <ul class="user-list">
                @for (user of userResults; track user.id) {
                  <li (click)="linkUserToClient(user.id)">
                    <span>{{ user.username }}</span> – <span class="badge">{{ user.role }}</span>
                  </li>
                }
              </ul>
            } @else if (userSearchTerm) {
              <p class="empty-state">Sin resultados</p>
            }
          </div>
        </div>
      </div>
    }
  `,
})
export class ClientList implements OnInit {
  clients: ClientResponse[] = [];
  loading = true;
  page = 0;
  size = 20;
  totalPages = 0;
  totalElements = 0;
  filterActive: boolean | undefined = undefined;
  searchTerm = '';

  editingId: number | null = null;
  editForm: ClientRequest = { name: '', phoneNumber: '' };

  showLinkModal = false;
  selectedClient: ClientResponse | null = null;
  userSearchTerm = '';
  userResults: UserResponse[] = [];

  constructor(
    private svc: ClientService,
    private userSvc: UserService,
    public router: Router,
    private toast: ToastService,
    private cdr: ChangeDetectorRef,
    private confirmSvc: ConfirmService
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.svc.getAll({ page: this.page, size: this.size, active: this.filterActive, search: this.searchTerm || undefined }).subscribe({
      next: p => {
        this.clients = p.content;
        this.totalPages = p.totalPages;
        this.totalElements = p.totalElements;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  applyFilter(): void { this.page = 0; this.load(); }
  prevPage(): void { if (this.page > 0) { this.page--; this.load(); } }
  nextPage(): void { if (this.page < this.totalPages - 1) { this.page++; this.load(); } }

  onSearch(): void {
    this.page = 0;
    this.load();
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.onSearch();
  }

  async deactivate(id: number): Promise<void> {
    const ok = await this.confirmSvc.confirm('¿Desactivar este cliente?');
    if (!ok) return;
    this.svc.deactivate(id).subscribe({
      next: () => { this.toast.show('Cliente desactivado'); this.load(); },
      error: () => this.toast.show('Error al desactivar cliente', 'error'),
    });
  }

  startEdit(client: ClientResponse): void {
    this.editingId = client.id;
    this.editForm = {
      name: client.name,
      phoneNumber: client.phoneNumber,
      gender: client.gender,
    };
  }

  cancelEdit(): void { this.editingId = null; }

  saveEdit(id: number): void {
    this.svc.update(id, this.editForm).subscribe({
      next: updated => {
        const idx = this.clients.findIndex(c => c.id === id);
        if (idx !== -1) this.clients[idx] = updated;
        this.cancelEdit();
        this.toast.show('Cliente actualizado');
        this.cdr.detectChanges();
      },
      error: err => this.toast.show(err.error?.message ?? 'Error al actualizar', 'error'),
    });
  }

  openLinkModal(client: ClientResponse): void {
    this.selectedClient = client;
    this.showLinkModal = true;
    this.userSearchTerm = '';
    this.userResults = [];
  }

  searchUsers(): void {
    if (!this.userSearchTerm.trim()) {
      this.userResults = [];
      return;
    }
    this.userSvc.getAll({ page: 0, size: 10 }).subscribe({
      next: page => {
        this.userResults = page.content.filter(u =>
          u.username.toLowerCase().includes(this.userSearchTerm.toLowerCase()) ||
          u.email.toLowerCase().includes(this.userSearchTerm.toLowerCase())
        );
        this.cdr.detectChanges();
      },
      error: () => this.toast.show('Error al buscar usuarios', 'error'),
    });
  }

  linkUserToClient(userId: number): void {
    if (!this.selectedClient) return;
    this.svc.linkUser(this.selectedClient.id, userId).subscribe({
      next: () => {
        this.toast.show('Usuario vinculado correctamente');
        this.showLinkModal = false;
        this.load();
      },
      error: () => this.toast.show('Error al vincular usuario', 'error'),
    });
  }

  async unlink(clientId: number): Promise<void> {
    const ok = await this.confirmSvc.confirm('¿Desvincular usuario de este cliente?');
    if (!ok) return;
    this.svc.unlinkUser(clientId).subscribe({
      next: () => {
        this.toast.show('Usuario desvinculado');
        this.load();
      },
      error: () => this.toast.show('Error al desvincular', 'error'),
    });
  }
}
