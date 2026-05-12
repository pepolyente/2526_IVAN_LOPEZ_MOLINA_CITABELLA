import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserList } from './user-list/user-list';

const routes: Routes = [
  { path: '',      redirectTo: 'users', pathMatch: 'full' },
  { path: 'users', component: UserList },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule {}
