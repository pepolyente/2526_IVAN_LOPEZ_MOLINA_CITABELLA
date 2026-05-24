import { Injectable, signal, effect } from '@angular/core';

type ThemePreference = 'dark' | 'light' | 'auto';

@Injectable({ providedIn: 'root' })
export class ThemeService {

  private readonly STORAGE_KEY = 'citabella-theme';
  private readonly html = document.documentElement;

  readonly isDark = signal(false);

  constructor() {
    const saved = localStorage.getItem(this.STORAGE_KEY) as ThemePreference | null;
    this.apply(saved ?? 'auto');

    window.matchMedia('(prefers-color-scheme: dark)')
      .addEventListener('change', (event) => {
        if (!localStorage.getItem(this.STORAGE_KEY)) {
          this.isDark.set(event.matches);
        }
      });
  }

  toggle(): void {
    this.apply(this.isDark() ? 'light' : 'dark');
  }

  setDark(): void  { this.apply('dark'); }
  setLight(): void { this.apply('light'); }
  setAuto(): void  { this.apply('auto'); }

  private apply(pref: ThemePreference): void {
    this.html.classList.remove('theme-dark', 'theme-light');

    if (pref === 'dark') {
      this.html.classList.add('theme-dark');
      this.isDark.set(true);
      localStorage.setItem(this.STORAGE_KEY, 'dark');
    } else if (pref === 'light') {
      this.html.classList.add('theme-light');
      this.isDark.set(false);
      localStorage.setItem(this.STORAGE_KEY, 'light');
    } else {
      localStorage.removeItem(this.STORAGE_KEY);
      this.isDark.set(window.matchMedia('(prefers-color-scheme: dark)').matches);
    }
  }
}
