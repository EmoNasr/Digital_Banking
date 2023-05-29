import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../model/customer.model";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  backEnd:string="http://localhost:8081"
  token!: any ;
  headers_object!: HttpHeaders;
  constructor(private http:HttpClient) {
    this.token =  localStorage.getItem("access_token")?.split('"').join('');
    this.headers_object = new HttpHeaders({
      'Authorization': 'Bearer '+this.token})
  }
  public getCustomers():Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(this.backEnd+"/customers",{headers:this.headers_object});
  }

  public saveCustomer(customer:Customer):Observable<Customer>{
    return this.http.post<Customer>(this.backEnd+"/customer",customer,{headers:this.headers_object});
  }

  public deleteCustomer(id:number):Observable<Customer>{
    return this.http.delete<Customer>(this.backEnd+"/customer/"+id,{headers:this.headers_object});
  }
  public searchCustomers(keyword:string):Observable<Array<Customer>>{
     return this.http.get<Array<Customer>>("http://localhost:8081/customers/search?keyword="+keyword,{headers:this.headers_object});
  };

}
