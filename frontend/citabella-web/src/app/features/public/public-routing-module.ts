import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Home } from './home/home';
import { ServicesPage } from './services-page/services-page';
import { ProductsPage } from './products-page/products-page';

const routes: Routes = [
  { path: '',          component: Home },
  { path: 'servicios', component: ServicesPage },
  { path: 'productos', component: ProductsPage },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PublicRoutingModule {}
