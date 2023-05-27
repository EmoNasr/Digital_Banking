import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Route, Router} from "@angular/router";
import {Customer} from "../model/customer.model";
import {AccountService} from "../services/account.service";
import {HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-customer-account',
  templateUrl: './customer-account.component.html',
  styleUrls: ['./customer-account.component.scss']
})
export class CustomerAccountComponent implements OnInit{
  customerID!:string;
  customer!:any;
  accountDetails!:any;
  private token!: string | null;
  private headers_object!: HttpHeaders;

  totalPage!:number;
  account$: any;
   currentPage: number = 1;
  constructor(private route:ActivatedRoute, private router:Router,private accountService:AccountService) {
    //data between componenent
    this.customer=this.router.getCurrentNavigation()?.extras.state as Customer;
  }
  ngOnInit(): void {
    this.token =  localStorage.getItem("access_token");
    this.headers_object = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': "Bearer "+this.token})


    console.log(this.customer)
   this.viewDetailsAccount()
  }

  public viewDetailsAccount(){
    this.accountService.getAccountDetails(this.customer.name,this.currentPage,8).subscribe({
        next:(data)=>{
          this.accountDetails = data;
          this.totalPage = data.length/8;
          console.log(data);
        },
        error:err => {
          console.log(err.message)
        }
      }
    )
  }
  goto(page: number) {
    this.currentPage = page;
    this.accountService.getAccountDetails(this.customer.name,this.currentPage,8).subscribe({
        next:(data)=>{
          this.accountDetails = data;
          console.log(data);
        },
        error:err => {
          console.log(err.message)
        }
      }
    )
  }

}
