import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { TreatmentService } from '../../../core/services/treatment.service';
import { TreatmentResponse } from '../../../shared/models/treatment.model';

@Component({
  selector: 'app-services-page',
  standalone: false,
  template: `
    <div class="services-page-wrapper">
      <div class="services-page-header">
        <span class="services-eyebrow">Nuestros tratamientos</span>
        <h1 class="services-page-title">Servicios de belleza premium</h1>
        <p class="services-page-sub">Descubre todos los tratamientos disponibles en CitaBella, diseñados para realzar tu belleza natural.</p>
      </div>

      @if (loading) {
        <div class="services-skeleton-grid">
          @for (i of [1,2,3,4,5,6]; track i) {
            <div class="service-skeleton-card">
              <div class="skel-icon"></div>
              <div class="skel-line skel-wide"></div>
              <div class="skel-line skel-medium"></div>
              <div class="skel-line skel-narrow"></div>
            </div>
          }
        </div>
      } @else if (treatments.length === 0) {
        <div class="services-empty">
          <span class="material-symbols-outlined">spa</span>
          <p>Próximamente nuevos tratamientos</p>
        </div>
      } @else {
        <div class="services-catalog-grid">
          @for (treatment of treatments; track treatment.id) {
            <div class="service-catalog-card">
              <div class="scc-icon-wrap">
                <span class="material-symbols-outlined">spa</span>
              </div>
              <div class="scc-content">
                <div class="scc-name">{{ treatment.name }}</div>
                <div class="scc-meta">
              <span class="scc-duration">
                <span class="material-symbols-outlined">schedule</span>
                {{ treatment.minimumDuration }} min
              </span>
                </div>
              </div>
              <div class="scc-price">{{ treatment.price | currency:'EUR' }}</div>
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
