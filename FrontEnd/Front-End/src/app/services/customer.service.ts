import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../model/customer.model";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  backEnd:string="http://localhost:8081/customer"
  constructor(private http:HttpClient) { }
  public getCustomers():Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(this.backEnd+"/customers");
  }

  public saveCustomer(customer:Customer,headers:any):Observable<Customer>{
    return this.http.post<Customer>(this.backEnd+"/customer",customer,{headers:headers});
  }

  public deleteCustomer(id:number,headers:any):Observable<Customer>{
    return this.http.delete<Customer>(this.backEnd+"/customer/"+id,{headers:headers});
  }
  public searchCustomers(keyword:string,headers:any):Observable<Array<Customer>>{
     return this.http.get<Array<Customer>>(this.backEnd+"/customers/search?keyword="+keyword,{headers:headers});
  };

}
