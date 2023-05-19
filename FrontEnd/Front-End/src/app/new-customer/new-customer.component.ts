import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Customer} from "../model/customer.model";
import {CustomerService} from "../services/customer.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrls: ['./new-customer.component.scss']
})
export class NewCustomerComponent implements OnInit{
  newCustomerFromGroup!:FormGroup;

  constructor(private fb:FormBuilder,private customerService:CustomerService,private route:Router) {
  }

  ngOnInit(): void {
    this.newCustomerFromGroup=this.fb.group(
      {
        name:this.fb.control(null,Validators.required),
        email:this.fb.control(null,[Validators.required,Validators.email])
      }
    )
  }


  handelSaveCustomer() {
    let customer:Customer = this.newCustomerFromGroup.value;
    console.log(customer);
    this.customerService.saveCustomer(customer).subscribe(
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
