import { Component, OnInit } from '@angular/core';
import { TreatmentService } from '../../../core/services/treatment.service';
import { TreatmentResponse } from '../../../shared/models/treatment.model';

@Component({
  selector: 'app-services-page',
  standalone: false,
  template: `
    <div class="page-wrapper">
      <h1>Nuestros servicios</h1>
      <div class="cards-grid">
        @for (treatment of treatments; track treatment.id) {
          <div class="card">
            <h3>{{ treatment.name }}</h3>
            <p>Duración mínima: {{ treatment.minimumDuration }} min</p>
            <strong>{{ treatment.price | currency: 'EUR' }}</strong>
          </div>
        }
      </div>
    </div>
  `,
})
export class ServicesPage implements OnInit {
  treatments: TreatmentResponse[] = [];
  constructor(private svc: TreatmentService) {}
  ngOnInit(): void { this.svc.getAll().subscribe(data => (this.treatments = data)); }
}
