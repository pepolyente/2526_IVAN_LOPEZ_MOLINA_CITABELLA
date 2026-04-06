import { Component, OnInit } from '@angular/core';
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
  `,
})
export class ClientList implements OnInit {
  clients: ClientResponse[] = [];

  constructor(private svc: ClientService, public router: Router) {
  }

  ngOnInit(): void {
    this.svc.getAll().subscribe(data => (this.clients = data));
  }
}
