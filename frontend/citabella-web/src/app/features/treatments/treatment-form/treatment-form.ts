import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TreatmentService } from '../../../core/services/treatment.service';
import { TreatmentRequest } from '../../../shared/models/treatment.model';

@Component({
  selector: 'app-treatment-form',
  standalone: false,
  template: `
    <div class="page-header modal-create">
      <h2>Nuevo tratamiento</h2>
      <button class="btn-outline" (click)="router.navigate(['/panel/treatments'])">Cancelar</button>
    </div>
    <form class="form-card" (ngSubmit)="submit()">
      <div class="form-group">
        <label>Nombre *</label>
        <input type="text" [(ngModel)]="form.name" name="name" required />
      </div>
      <div class="form-group">
        <label>Descripción</label>
        <textarea [(ngModel)]="form.description" name="description" rows="2"></textarea>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>Duración mín. (min) *</label>
          <input type="number" [(ngModel)]="form.minimumDuration" name="minimumDuration" required min="1" />
        </div>
        <div class="form-group">
          <label>Duración máx. (min)</label>
          <input type="number" [(ngModel)]="form.maximumDuration" name="maximumDuration" min="1" />
        </div>
      </div>
      <div class="form-group">
        <label>Precio (€) *</label>
        <input type="number" [(ngModel)]="form.price" name="price" required min="0" step="0.01" />
      </div>
      @if (error) { <p class="error-msg">{{ error }}</p> }
      <button type="submit" class="btn-primary" [disabled]="loading">
        {{ loading ? 'Guardando...' : 'Crear tratamiento' }}
      </button>
    </form>
  `,
})
export class TreatmentForm {
  form: TreatmentRequest = { name: '', minimumDuration: 30, price: 0 };
  loading = false;
  error   = '';
  constructor(private svc: TreatmentService, public router: Router) {}
  submit(): void {
    this.loading = true;
    this.svc.create(this.form).subscribe({
      next: () => this.router.navigate(['/panel/treatments']),
      error: err => { this.error = err.error?.message ?? 'Error'; this.loading = false; },
    });
  }
}
