import { Injectable } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { filter } from 'rxjs/operators';

export interface Breadcrumb {
  label: string;
  url: string;
}

const ROUTE_LABELS: Record<string, string> = {
  panel:            'Panel',
  appointments:     'Agenda',
  'my-appointments':'Mis citas',
  clients:          'Clientes',
  employees:        'Equipo',
  treatments:       'Servicios',
  products:         'Productos',
  admin:            'Administración',
  users:            'Usuarios',
  new:              'Nuevo',
  list:             'Lista',
};

@Injectable({ providedIn: 'root' })
export class BreadcrumbService {

  private readonly _crumbs$ = new BehaviorSubject<Breadcrumb[]>([]);
  readonly crumbs$ = this._crumbs$.asObservable();

  constructor(private router: Router, private route: ActivatedRoute) {
    this.router.events
      .pipe(filter(e => e instanceof NavigationEnd))
      .subscribe(() => this._build());
    this._build();
  }

  private _build(): void {
    const segments = this.router.url.split('?')[0].split('/').filter(Boolean);
    const crumbs: Breadcrumb[] = [];
    let path = '';

    for (const seg of segments) {
      path += `/${seg}`;
      const label = ROUTE_LABELS[seg] ?? this._humanize(seg);
      crumbs.push({ label, url: path });
    }
    this._crumbs$.next(crumbs);
  }

  private _humanize(seg: string): string {
    if (/^\d+$/.test(seg)) return `#${seg}`;
    return seg.charAt(0).toUpperCase() + seg.slice(1).replace(/-/g, ' ');
  }
}
