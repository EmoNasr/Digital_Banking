import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../model/customer.model";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  backEnd:string="http://localhost:8081"
  constructor(private http:HttpClient) { }
  public getCustomers():Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(this.backEnd+"/customers");
  }

  public searchCustomers(keyword:string):Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(this.backEnd+"/customers/search?keyword="+keyword);
  }

  public saveCustomer(customer:Customer):Observable<Customer>{
    return this.http.post<Customer>(this.backEnd+"/customer",customer);
  }

  public deleteCustomer(id:number):Observable<Customer>{
    return this.http.delete<Customer>(this.backEnd+"/customer/"+id);
  }
}
