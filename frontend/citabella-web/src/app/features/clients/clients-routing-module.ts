import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClientList } from './client-list/client-list';
import { ClientForm } from './client-form/client-form';

const routes: Routes = [
  { path: '',    component: ClientList },
  { path: 'new', component: ClientForm },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ClientsRoutingModule {}
