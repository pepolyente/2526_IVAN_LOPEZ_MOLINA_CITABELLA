import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClientsRoutingModule } from './clients-routing-module';
import { ClientList } from './client-list/client-list';
import { ClientForm } from './client-form/client-form';
import {SharedModule} from "../../shared/shared-module";

@NgModule({
  declarations: [ClientList, ClientForm],
    imports: [CommonModule, FormsModule, ClientsRoutingModule, SharedModule],
})
export class ClientsModule {}
