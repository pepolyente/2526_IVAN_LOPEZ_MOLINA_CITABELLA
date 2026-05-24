import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AppointmentResponse } from '../../../shared/models/appointment.model';

@Component({
  selector: 'app-my-appointment-modal',
  standalone: false,
  template: `
    <div class="modal-overlay" (click)="close.emit()">
      <div class="modal-card" (click)="$event.stopPropagation()">
        <div class="modal-header">
          <h3>Detalle de cita</h3>
          <button class="modal-close" (click)="close.emit()">✕</button>
        </div>
        <div class="modal-body">
          <div class="detail-grid">
            <div class="detail-row">
              <span class="detail-label">Hora</span>
              <span>{{ appointment.startAt | date:'dd/MM/yyyy HH:mm' }} → {{ appointment.endAt | date:'HH:mm' }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Empleado</span>
              <span>{{ appointment.employee.name }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Servicios</span>
              <span>
                @for (t of appointment.treatments; track t.id) {
                  <span class="badge">{{ t.name }}</span>
                }
              </span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Estado</span>
              <span class="badge badge-{{ appointment.status | lowercase }}">{{ appointment.status }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .modal-overlay {
      position: fixed; inset: 0;
      background: rgba(0,0,0,0.5);
      backdrop-filter: blur(4px);
      z-index: 1000;
      display: flex; align-items: center; justify-content: center;
      padding: 16px;
      animation: fadeIn 0.15s ease;
    }
    @keyframes fadeIn { from { opacity: 0 } to { opacity: 1 } }

    .modal-card {
      background: var(--color-surface);
      border: 1px solid var(--color-border);
      border-radius: 16px;
      width: 100%; max-width: 480px;
      box-shadow: var(--shadow-card-lg);
      animation: slideUp 0.2s ease;
    }
    @keyframes slideUp {
      from { transform: translateY(20px); opacity: 0 }
      to   { transform: translateY(0);    opacity: 1 }
    }

    .modal-header {
      display: flex; align-items: center; justify-content: space-between;
      padding: 20px 24px 16px;
      border-bottom: 1px solid var(--color-border);
    }
    .modal-header h3 { font-size: 17px; font-weight: 700; }
    .modal-close {
      background: none; border: none; font-size: 18px; cursor: pointer;
      color: var(--color-text-muted); padding: 4px 8px; border-radius: 6px;
    }
    .modal-close:hover { background: var(--color-surface-alt); }

    .modal-body { padding: 20px 24px 24px; }

    .detail-grid { display: flex; flex-direction: column; gap: 0; }
    .detail-row {
      display: flex; align-items: flex-start; gap: 12px;
      padding: 10px 0;
      border-bottom: 1px solid var(--color-border);
      font-size: 14px;
    }
    .detail-row:last-child { border-bottom: none; }
    .detail-label { color: var(--color-text-muted); font-weight: 500; min-width: 90px; }
  `]
})
export class MyAppointmentModal {
  @Input() appointment!: AppointmentResponse;
  @Output() close = new EventEmitter<void>();
}
