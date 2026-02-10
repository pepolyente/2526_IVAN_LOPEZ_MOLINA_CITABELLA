import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SimpleTable } from './components/simple-table/simple-table';



@NgModule({
  declarations: [
    SimpleTable
  ],
  imports: [
    CommonModule
  ],
  exports: [
    SimpleTable
  ]
})
export class SharedModule { }
