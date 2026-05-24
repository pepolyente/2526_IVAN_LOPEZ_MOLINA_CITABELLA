import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { ProductList } from './product-list/product-list';
import { ProductForm } from './product-form/product-form';

const routes: Routes = [
  { path: '', component: ProductList },
  { path: 'new', component: ProductForm }
];

@NgModule({
  declarations: [ProductList, ProductForm],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(routes),
  ],
})
export class ProductModule {}
