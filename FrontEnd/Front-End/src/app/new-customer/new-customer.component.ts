import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Customer} from "../model/customer.model";
import {CustomerService} from "../services/customer.service";
import {Router} from "@angular/router";
import {HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrls: ['./new-customer.component.scss']
})
export class NewCustomerComponent implements OnInit{
  newCustomerFromGroup!:FormGroup;
  private token!: string | null;
  private headers_object!: HttpHeaders;

  constructor(private fb:FormBuilder,private customerService:CustomerService,private route:Router) {
  }

  ngOnInit(): void {
    this.newCustomerFromGroup=this.fb.group(
      {
        name:this.fb.control(null,Validators.required),
        email:this.fb.control(null,[Validators.required,Validators.email])
      }
    )
    this.token =  localStorage.getItem("access_token");
    this.headers_object = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': "Bearer "+this.token})
  }


  handelSaveCustomer() {
    let customer:Customer = this.newCustomerFromGroup.value;
    console.log(customer);
    this.customerService.saveCustomer(customer,this.headers_object).subscribe(
      {
        next:data=>{
          alert("Customer has been add succeddfully");
          this.newCustomerFromGroup.reset();
          this.route.navigateByUrl("/customers");
        },
        error: err => {
          console.log(err);
        }
      }
    )

  }
}
