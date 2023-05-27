import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CustomerService} from "../services/customer.service";
import {catchError, map, Observable, throwError} from "rxjs";
import {Customer} from "../model/customer.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../services/authentication.service";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.scss']
})
export class CustomersComponent implements OnInit{

  customers!:Observable<Array<Customer>>;
  errorMessage!:string;
  searchFormGroup!:FormGroup;

  private token!:any;
  private headers_object!:any;

  constructor(private customersService:CustomerService,private fb:FormBuilder,private route:Router,public auth:AuthenticationService) {
  }

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group(
      {
        keyword:this.fb.control("")
      }
    )

    this.token =  localStorage.getItem("access_token");
    this.headers_object = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': "Bearer "+this.token})


   this.handleSearchCustomer();
  }

  handleSearchCustomer() {
    let keyword = this.searchFormGroup?.value.keyword;

    this.customers=this.customersService.searchCustomers(keyword,this.headers_object)
        .pipe(
          catchError(
            err => {
              this.errorMessage = err.message;
              return throwError(err);
            }
          )
        );
  }

  handelDeleteCustomer(c: Customer) {
    let conf = confirm("Are you sure");
    if (!conf) return ;
    this.customersService.deleteCustomer(c.id,this.headers_object).subscribe(
      {
        next:(res)=>{
          this.customers=this.customers.pipe(
            map(data=> {
              let index = data.indexOf(c);
              data.slice(index, 1);
              return data;
            }
          ))
        },
        error:err => {
          console.log(err);
        }
      }
    )

  }

  handelCustomerAccount(c: Customer) {
    this.route.navigateByUrl("/admin/customer-account/"+c.name,{state:c});
  }
}
