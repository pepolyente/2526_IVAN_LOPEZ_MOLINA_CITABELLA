import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TreatmentService } from '../../../core/services/treatment.service';
import { TreatmentResponse } from '../../../shared/models/treatment.model';

@Component({
  selector: 'app-treatment-list',
  standalone: false,
  template: `
    <div class="page-header">
      <h2>Tratamientos</h2>
      <button class="btn-primary" (click)="router.navigate(['/panel/treatments/new'])">+ Nuevo</button>
    </div>
    @if (loading) {
      <p class="empty-state">Cargando...</p>
    } @else if (treatments.length === 0) {
      <p class="empty-state">No hay tratamientos registrados.</p>
    } @else {
      <table class="simple-table">
        <thead>
        <tr>
          <th>Nombre</th>
          <th>Min. duración</th>
          <th>Precio</th>
          <th>Activo</th>
        </tr>
        </thead>
        <tbody>
          @for (treatment of treatments; track treatment.id) {
            <tr>
              <td>{{ treatment.name }}</td>
              <td>{{ treatment.minimumDuration }} min</td>
              <td>{{ treatment.price | currency:'EUR' }}</td>
              <td>{{ treatment.active ? '✅' : '❌' }}</td>
            </tr>
          }
        </tbody>
      </table>
    }
  `,
})
export class TreatmentList implements OnInit {
  treatments: TreatmentResponse[] = [];
  loading = true;

  constructor(private svc: TreatmentService, public router: Router, private changeDetectorRef: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.svc.getAll().subscribe({
      next: data => {
        this.treatments = data;
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
