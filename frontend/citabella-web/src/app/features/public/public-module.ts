import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PublicRoutingModule } from './public-routing-module';
import { Home } from './home/home';
import { ServicesPage } from './services-page/services-page';
import { ProductsPage } from './products-page/products-page';

@NgModule({
  declarations: [Home, ServicesPage, ProductsPage],
  imports: [CommonModule, PublicRoutingModule],
})
export class PublicModule {}
