import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { ProductList } from './product-list/product-list';

const routes: Routes = [
  { path: '', component: ProductList },
];

@NgModule({
  declarations: [ProductList],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(routes),
  ],
})
export class ProductModule {}
