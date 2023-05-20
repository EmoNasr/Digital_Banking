import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CustomersComponent} from "./customers/customers.component";
import {AccountsComponent} from "./accounts/accounts.component";
import {NewCustomerComponent} from "./new-customer/new-customer.component";
import {CustomerAccountComponent} from "./customer-account/customer-account.component";
import {LoginComponent} from "./login/login.component";

const routes: Routes = [
  {
    path:"login",component:LoginComponent
  },
  {
    path:"",component:LoginComponent
  },
  {
    path:"customers",component:CustomersComponent
  },
  {
    path:"accounts",component:AccountsComponent
  },
  {
    path:"new_customer",component:NewCustomerComponent
  },
  {
    path:"customer-account/:id",component:CustomerAccountComponent
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
