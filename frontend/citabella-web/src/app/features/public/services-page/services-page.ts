import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { TreatmentService } from '../../../core/services/treatment.service';
import { TreatmentResponse } from '../../../shared/models/treatment.model';

@Component({
  selector: 'app-services-page',
  standalone: false,
  template: `
    <div class="page-wrapper">
      <h1>Nuestros servicios</h1>
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
      } @else if (treatments.length === 0) {
        <p class="empty-state">Próximamente...</p>
      } @else {
        <div class="cards-grid">
          @for (treatment of treatments; track treatment.id) {
            <div class="card">
              <h3>{{ treatment.name }}</h3>
              <p>Duración mínima: {{ treatment.minimumDuration }} min</p>
              <strong>{{ treatment.price | currency:'EUR' }}</strong>
            </div>
          }
        </div>
      }
    </div>
  `,
})
export class ServicesPage implements OnInit {
  treatments: TreatmentResponse[] = [];
  loading = true;

  constructor(private svc: TreatmentService, private changeDetectorRef: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.svc.getAll({ page: 0, size: 100 }).subscribe({
      next: data => {
        this.treatments = data.content;
        this.loading    = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => { this.loading = false; this.changeDetectorRef.detectChanges(); }
    });
  }
}
