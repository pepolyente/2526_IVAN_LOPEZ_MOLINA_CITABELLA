import { Component, OnInit } from '@angular/core';
import { BreadcrumbService, Breadcrumb } from '../../../core/services/breadcrumb.service';

@Component({
  selector: 'app-breadcrumbs',
  standalone: false,
  template: `
    @if (crumbs.length > 1) {
      <nav class="breadcrumbs" aria-label="Navegación">
        @for (crumb of crumbs; track crumb.url; let last = $last) {
          @if (!last) {
            <a class="crumb" [routerLink]="crumb.url">{{ crumb.label }}</a>
            <span class="crumb-sep" aria-hidden="true">›</span>
          } @else {
            <span class="crumb crumb-active" aria-current="page">{{ crumb.label }}</span>
          }
        }
      </nav>
    }
  `,
})
export class BreadcrumbsComponent implements OnInit {
  crumbs: Breadcrumb[] = [];

  constructor(private svc: BreadcrumbService) {}

  ngOnInit(): void {
    this.svc.crumbs$.subscribe(c => this.crumbs = c);
  }
}
