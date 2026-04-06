import { Component, OnInit } from '@angular/core';
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
    <table class="simple-table">
      <thead><tr><th>Nombre</th><th>Min. duración</th><th>Precio</th><th>Activo</th></tr></thead>
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
  `,
})
export class TreatmentList implements OnInit {
  treatments: TreatmentResponse[] = [];
  constructor(private svc: TreatmentService, public router: Router) {}
  ngOnInit(): void { this.svc.getAll().subscribe(data => (this.treatments = data)); }
}
