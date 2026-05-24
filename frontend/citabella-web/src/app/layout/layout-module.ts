import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../shared/shared-module';
import { Header } from './header/header';
import { MainLayout } from './main-layout/main-layout';
import { PublicLayout } from './public-layout/public-layout';
import { PublicHeader } from './public-header/public-header';

@NgModule({
  declarations: [Header, MainLayout, PublicLayout, PublicHeader],
  imports: [CommonModule, RouterModule, SharedModule],
  exports: [MainLayout, PublicLayout],
})
export class LayoutModule {}

