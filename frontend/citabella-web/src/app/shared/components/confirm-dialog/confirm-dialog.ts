import { Component } from '@angular/core';
import { ConfirmService } from '../../../core/services/confirm.service';

@Component({
  selector: 'app-confirm-dialog',
  standalone: false,
  template: `
    @if (confirmSvc.state(); as data) {
      <div class="modal-overlay" (click)="close(false)">
        <div class="modal-card" (click)="$event.stopPropagation()">
          <p class="confirm-message">{{ data.message }}</p>
          <div class="modal-actions">
            <button class="btn-cancel" (click)="close(false)">
              {{ data.cancelLabel }}
            </button>
            <button class="btn-confirm" (click)="close(true)">
              {{ data.confirmLabel }}
            </button>
          </div>
        </div>
      </div>
    }
  `,
  styles: [`
    .modal-overlay {
      position: fixed; inset: 0;
      background: rgba(0,0,0,0.5);
      backdrop-filter: blur(4px);
      z-index: 10000;
      display: flex; align-items: center; justify-content: center;
      padding: 16px;
      animation: fadeIn 0.15s ease;
    }
    @keyframes fadeIn { from { opacity: 0 } to { opacity: 1 } }

    .modal-card {
      background: var(--color-surface);
      border: 1px solid var(--color-border);
      border-radius: var(--radius);
      padding: 24px;
      max-width: 400px;
      width: 100%;
      box-shadow: var(--shadow-card-lg);
      animation: slideUp 0.2s ease;
    }
    @keyframes slideUp {
      from { transform: translateY(20px); opacity: 0 }
      to   { transform: translateY(0);    opacity: 1 }
    }

    .confirm-message {
      font-size: 15px;
      color: var(--color-text);
      margin-bottom: 24px;
      line-height: 1.5;
    }

    .modal-actions {
      display: flex;
      justify-content: flex-end;
      gap: 8px;
    }

    .btn-cancel {
      padding: 8px 16px;
      border: 1px solid var(--color-border);
      background: var(--color-surface);
      color: var(--color-text);
      border-radius: var(--radius);
      font-size: 14px;
      font-family: inherit;
      cursor: pointer;
      transition: background 0.2s;
    }
    .btn-cancel:hover {
      background: var(--color-surface-hover);
    }

    .btn-confirm {
      padding: 8px 16px;
      border: none;
      background: var(--color-danger);
      color: var(--color-text-inverted);
      border-radius: var(--radius);
      font-size: 14px;
      font-weight: 500;
      font-family: inherit;
      cursor: pointer;
      transition: opacity 0.2s;
    }
    .btn-confirm:hover {
      opacity: 0.9;
    }
  `]
})
export class ConfirmDialogComponent {
  constructor(public confirmSvc: ConfirmService) {}

  close(result: boolean): void {
    this.confirmSvc.resolve(result);
  }
}
