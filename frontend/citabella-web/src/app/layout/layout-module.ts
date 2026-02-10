import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Header } from './header/header';
import { MainLayout } from './main-layout/main-layout';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    Header,
    MainLayout
  ],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [
    MainLayout
  ]
})
export class LayoutModule {}

