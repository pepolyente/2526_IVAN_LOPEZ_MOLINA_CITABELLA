import {ChangeDetectorRef, Component} from '@angular/core';
import { Router } from '@angular/router';
import { ClientService } from '../../../core/services/client.service';
import { ClientRequest } from '../../../shared/models/client.model';

@Component({
  selector: 'app-client-form',
  standalone: false,
  template: `
    <div class="page-header modal-create">
      <h2>Nuevo cliente</h2>
      <button class="btn-outline" (click)="router.navigate(['/panel/clients'])">Cancelar</button>
    </div>
    <form class="form-card" (ngSubmit)="submit()">
      <div class="form-group">
        <label>Nombre *</label>
        <input type="text" [(ngModel)]="form.name" name="name" required />
      </div>
      <div class="form-group">
        <label>Teléfono *</label>
        <input type="tel" [(ngModel)]="form.phoneNumber" name="phoneNumber" required />
      </div>
      <div class="form-group">
        <label>Fecha de nacimiento</label>
        <input type="text" appFlatpickr  [(ngModel)]="form.birthday" name="birthday" />
      </div>
      <div class="form-group">
        <label>Género</label>
        <select [(ngModel)]="form.gender" name="gender">
          <option value="" disabled selected hidden>
            Sin especificar
          </option>
          <option value="MALE">Hombre</option>
          <option value="FEMALE">Mujer</option>
          <option value="OTHER">Otro</option>
        </select>
      </div>
      @if (error) { <p class="error-msg">{{ error }}</p> }
      <button type="submit" class="btn-primary" [disabled]="loading">
        {{ loading ? 'Guardando...' : 'Crear cliente' }}
      </button>
    </form>
  `,
})
export class ClientForm {
  form: ClientRequest = { name: '', phoneNumber: '' };
  loading = false;
  error   = '';
  constructor(private svc: ClientService, public router: Router, private changeDetectorRef: ChangeDetectorRef) {}
  submit(): void {
    this.loading = true;
    this.svc.create(this.form).subscribe({
      next: () => this.router.navigate(['/panel/clients']),
      error: err => { this.error = err.error?.message ?? 'Error'; this.loading = false; },
    });
  }
}
