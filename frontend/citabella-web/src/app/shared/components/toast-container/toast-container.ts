import { Component } from '@angular/core';
import {ToastService} from '../../../core/services/toast.service';

@Component({
  selector: 'app-toast-container',
  standalone: false,
  template: `
    <div class="toast-stack">
      @for (t of toast.toasts(); track t.id) {
        <div class="toast toast-{{ t.type }}" (click)="toast.dismiss(t.id)">
          <span class="material-symbols-outlined">
            {{ t.type === 'success' ? 'check_circle' : t.type === 'error' ? 'error' : 'warning' }}
          </span>
          {{ t.message }}
        </div>
      }
    </div>
  `,
  styles: [`
    .toast-stack {
      position: fixed; bottom: 24px; right: 24px;
      display: flex; flex-direction: column; gap: 8px;
      z-index: 9999; pointer-events: none;
    }
    .toast {
      display: flex; align-items: center; gap: 8px;
      padding: 12px 16px; border-radius: var(--radius);
      font-size: 14px; font-weight: 500;
      box-shadow: var(--shadow-card-lg);
      animation: slideInRight 0.25s ease;
      cursor: pointer; pointer-events: auto;
      max-width: 320px;
    }
    @keyframes slideInRight {
      from { transform: translateX(100%); opacity: 0 }
      to   { transform: translateX(0); opacity: 1 }
    }
    .toast-success { background: var(--badge-confirmed-bg); color: var(--badge-confirmed-text); }
    .toast-error   { background: var(--badge-cancelled-bg); color: var(--badge-cancelled-text); }
    .toast-warning { background: var(--badge-pending-bg);   color: var(--badge-pending-text); }
  `]
})
export class ToastContainerComponent {
  constructor(public toast: ToastService) {}
}
