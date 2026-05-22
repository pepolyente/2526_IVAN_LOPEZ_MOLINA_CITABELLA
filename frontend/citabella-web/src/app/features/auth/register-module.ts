import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { Register } from './register/register';

const routes: Routes = [
  { path: '', component: Register },
];

@NgModule({
  declarations: [Register],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(routes),
  ],
})
export class RegisterModule {}
