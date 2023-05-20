import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Route, Router} from "@angular/router";
import {Customer} from "../model/customer.model";

@Component({
  selector: 'app-customer-account',
  templateUrl: './customer-account.component.html',
  styleUrls: ['./customer-account.component.scss']
})
export class CustomerAccountComponent implements OnInit{
  customerID!:string;
  customer!:Customer;
  constructor(private route:ActivatedRoute, private router:Router) {
    this.customer=this.router.getCurrentNavigation()?.extras as Customer;
  }
  ngOnInit(): void {
    this.customerID= this.route.snapshot.params["id"];
  }

}
