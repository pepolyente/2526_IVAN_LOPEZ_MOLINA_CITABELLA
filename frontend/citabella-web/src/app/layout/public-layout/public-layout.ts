import { Component } from '@angular/core';

@Component({
  selector: 'app-public-layout',
  standalone: false,
  template: `
    <app-public-header></app-public-header>
    <main class="public-content">
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`.public-content { padding: 0; }`]
})
export class PublicLayout {}
