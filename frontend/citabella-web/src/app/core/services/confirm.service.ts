import { Injectable, signal } from '@angular/core';

export interface ConfirmDialogConfig {
  message: string;
  confirmLabel?: string;
  cancelLabel?: string;
  resolve: (result: boolean) => void;
}

@Injectable({ providedIn: 'root' })
export class ConfirmService {
  private readonly _state = signal<ConfirmDialogConfig | null>(null);
  readonly state = this._state.asReadonly();

  confirm(message: string, options?: {
    confirmLabel?: string;
    cancelLabel?: string;
  }): Promise<boolean> {
    return new Promise<boolean>(resolve => {
      this._state.set({
        message,
        confirmLabel: options?.confirmLabel ?? 'Confirmar',
        cancelLabel: options?.cancelLabel ?? 'Cancelar',
        resolve,
      });
    });
  }

  resolve(result: boolean): void {
    const current = this._state();
    if (current) {
      current.resolve(result);
      this._state.set(null);
    }
  }
}
