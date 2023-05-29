import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountDetails} from "../model/account.model";

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  backend:string="http://localhost:8081";
  private token!: string | null ;
  private headers_object!: HttpHeaders;
  constructor(private http:HttpClient) {

    this.token =  localStorage.getItem("access_token");
    this.headers_object = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': "Bearer "+this.token?.split('"').join('')})
  }

  public getAccount(id:string,page:number,size:number):Observable<AccountDetails>{
    return this.http.get<AccountDetails>(this.backend+"/accounts/"+id+"/pageOperations?page="+page+"&size="+size,{headers:this.headers_object})
  }
  public getAccountsId(page:number,size:number):Observable<any>{
    return this.http.get<any>(this.backend+"/accounts/pageOperations?page="+page+"&size="+size,{headers:this.headers_object})
  }

  public getAllAccounts():Observable<AccountDetails>{
    return this.http.get<AccountDetails>(this.backend+"/accounts",{headers:this.headers_object});
  }
  public getAccountDetails(name:string,page:number,size:number):Observable<any>{
    return this.http.get<any>(this.backend+"/accountHistory/"+name+"?page="+page+"&size="+size,{headers:this.headers_object});
  }

  public debit( accountID:string,amount:number,description:string){
    return this.http.post(this.backend+"/accounts/debit"
      ,{accountID:accountID,amount:amount,description:description},
      {headers:this.headers_object}
    );
  }
  public credit( accountID:string,amount:number,description:string){
    return this.http.post(this.backend+"/accounts/credit"
      ,{accountID:accountID,amount:amount,description:description},
      {headers:this.headers_object}
    );
  }
  public transfer( accountSource:string,accountDestination:string,amount:number,description:string){
    return this.http.post(this.backend+"/accounts/transfer"
      ,{accountSource:accountSource,accountDestination:accountDestination,amount:amount,description:description},
      {headers:this.headers_object}
    );
  }
}
