import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountDetails} from "../model/account.model";

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  backend:string="http://localhost:8081";
  constructor(private http:HttpClient) { }

  public getAccount(id:string,page:number,size:number):Observable<AccountDetails>{
    return this.http.get<AccountDetails>(this.backend+"/accounts/"+id+"/pageOperations?page="+page+"&size="+size)
  }

  public debit( accountID:string,amount:number,description:string){
    return this.http.post(this.backend+"/accounts/debit"
      ,{accountID:accountID,amount:amount,description:description}
    );
  }
  public credit( accountID:string,amount:number,description:string){
    return this.http.post(this.backend+"/accounts/credit"
      ,{accountID:accountID,amount:amount,description:description}
    );
  }
  public transfer( accountSource:string,accountDestination:string,amount:number,description:string){
    return this.http.post(this.backend+"/accounts/transfer"
      ,{accountSource:accountSource,accountDestination:accountDestination,amount:amount,description:description}
    );
  }
}
