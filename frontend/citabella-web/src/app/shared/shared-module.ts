import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { BreadcrumbsComponent } from './components/breadcrumbs/breadcrumbs';
import { ToastContainerComponent } from './components/toast-container/toast-container';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog';

import { FlatpickrDirective } from './directives/flatpickr.directive';

@NgModule({
  declarations: [
    BreadcrumbsComponent,
    ToastContainerComponent,
    ConfirmDialogComponent,
    FlatpickrDirective
  ],

  imports: [
    CommonModule,
    RouterModule,
  ],

  exports: [
    BreadcrumbsComponent,
    ToastContainerComponent,
    ConfirmDialogComponent,
    FlatpickrDirective
  ],
})
export class SharedModule {}
