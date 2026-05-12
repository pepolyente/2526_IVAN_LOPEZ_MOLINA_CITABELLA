import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BreadcrumbsComponent } from './components/breadcrumbs/breadcrumbs';
import { ToastContainerComponent } from './components/toast-container/toast-container';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog';

@NgModule({
  declarations: [
    BreadcrumbsComponent,
    ToastContainerComponent,
    ConfirmDialogComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
  ],
  exports: [
    BreadcrumbsComponent,
    ToastContainerComponent,
    ConfirmDialogComponent,
  ],
})
export class SharedModule {}
