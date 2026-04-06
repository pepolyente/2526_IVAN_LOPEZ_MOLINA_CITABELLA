import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ClientService } from '../../../core/services/client.service';
import { ClientResponse } from '../../../shared/models/client.model';

@Component({
  selector: 'app-client-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Clientes</h2>
      <button class="btn-primary" (click)="router.navigate(['/panel/clients/new'])">+ Nuevo cliente</button>
    </div>
    @if (loading) {
      <p class="empty-state">Cargando...</p>
    } @else if (clients.length === 0) {
      <p class="empty-state">No hay clientes registrados.</p>
    } @else {
      <table class="simple-table">
        <thead>
        <tr>
          <th>Nombre</th>
          <th>Teléfono</th>
          <th>Género</th>
        </tr>
        </thead>
        <tbody>
          @for (client of clients; track client.id) {
            <tr>
              <td>{{ client.name }}</td>
              <td>{{ client.phoneNumber }}</td>
              <td>{{ client.gender ?? '–' }}</td>
            </tr>
          }
        </tbody>
      </table>
    }
  `,
})
export class ClientList implements OnInit {
  clients: ClientResponse[] = [];
  loading = true;

  constructor(private svc: ClientService, public router: Router, private changeDetectorRef: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.svc.getAll().subscribe({
      next: data => {
        this.clients = data;
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      }
    });
  }
}
