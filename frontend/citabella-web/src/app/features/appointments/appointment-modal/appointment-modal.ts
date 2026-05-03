import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AppointmentService } from '../../../core/services/appointment.service';
import { AppointmentResponse, AppointmentStatus } from '../../../shared/models/appointment.model';

@Component({
  selector: 'app-appointment-modal',
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
              <span class="detail-label">🕐 Hora</span>
              <span>{{ appointment.startAt | date:'dd/MM/yyyy HH:mm' }} → {{ appointment.endAt | date:'HH:mm' }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">👤 Cliente</span>
              <span>{{ appointment.client?.name ?? '—' }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">✂ Empleado</span>
              <span>{{ appointment.employee?.name ?? '—' }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">💅 Servicios</span>
              <span>
                @for (t of appointment.treatments; track t.id) {
                  <span class="badge">{{ t.name }}</span>
                }
              </span>
            </div>
            <div class="detail-row">
              <span class="detail-label">📋 Estado</span>
              <span class="badge badge-{{ appointment.status | lowercase }}">{{ appointment.status }}</span>
            </div>
            @if (appointment.hasOverlap) {
              <div class="detail-row overlap-warn">
                <span>⚠️ Esta cita se solapa con otra</span>
              </div>
            }
          </div>

          @if (!isFinal && !showStatusChange) {
            <div class="action-row">
              <button class="btn-outline" (click)="showStatusChange = true">Cambiar estado</button>
              <button class="btn-xs btn-danger" (click)="cancelAppointment()">Cancelar cita</button>
            </div>
          }

          @if (showStatusChange) {
            <div class="status-change">
              <select [(ngModel)]="newStatus" class="status-select">
                <option value="">— Selecciona estado —</option>
                @for (s of allowedTransitions; track s) {
                  <option [value]="s">{{ s }}</option>
                }
              </select>
              <button class="btn-primary" (click)="confirmStatus()" [disabled]="!newStatus || loading">
                {{ loading ? '...' : 'Confirmar' }}
              </button>
              <button class="btn-outline" (click)="showStatusChange = false; newStatus = ''">Cancelar</button>
            </div>
          }

          @if (error) {
            <p class="error-msg">{{ error }}</p>
          }
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
    .overlap-warn { color: var(--color-warning); font-weight: 500; }

    .action-row { display: flex; gap: 8px; margin-top: 16px; flex-wrap: wrap; }

    .status-change {
      display: flex; gap: 8px; align-items: center; margin-top: 16px; flex-wrap: wrap;
    }
    .status-select {
      flex: 1; min-width: 160px;
      padding: 8px 12px;
      border: 1px solid var(--color-border); border-radius: var(--radius);
      font-size: 13px; font-family: inherit; outline: none;
    }
    .status-select:focus { border-color: var(--color-primary); }
  `],
})
export class AppointmentModal {
  @Input() appointment!: AppointmentResponse;
  @Output() close   = new EventEmitter<void>();
  @Output() updated = new EventEmitter<void>();

  showStatusChange = false;
  newStatus        = '';
  loading          = false;
  error            = '';

  private readonly transitions: Partial<Record<AppointmentStatus, AppointmentStatus[]>> = {
    PENDING:     ['CONFIRMED', 'CANCELLED', 'NO_SHOW'],
    CONFIRMED:   ['IN_PROGRESS', 'CANCELLED', 'NO_SHOW'],
    IN_PROGRESS: ['COMPLETED', 'CANCELLED'],
  };

  constructor(private svc: AppointmentService) {}

  get allowedTransitions(): AppointmentStatus[] {
    return this.transitions[this.appointment.status] ?? [];
  }

  get isFinal(): boolean {
    return ['CANCELLED', 'COMPLETED', 'NO_SHOW'].includes(this.appointment.status);
  }

  confirmStatus(): void {
    if (!this.newStatus) return;
    this.loading = true;
    this.error   = '';
    this.svc.changeStatus(this.appointment.id, this.newStatus).subscribe({
      next: () => { this.loading = false; this.updated.emit(); },
      error: () => { this.error = 'Error al cambiar el estado'; this.loading = false; },
    });
  }

  cancelAppointment(): void {
    if (!confirm('¿Cancelar esta cita?')) return;
    this.loading = true;
    this.svc.cancel(this.appointment.id).subscribe({
      next: () => { this.loading = false; this.updated.emit(); },
      error: () => { this.error = 'Error al cancelar la cita'; this.loading = false; },
    });
  }
}
