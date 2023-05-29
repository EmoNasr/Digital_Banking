import { Injectable } from '@angular/core';
import {AppUser} from "../model/User-model";
import {Observable, of, throwError} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private  ENDPOINT = "http://localhost:8081";
  Data:any;
  public authenticatedUser: AppUser|undefined;
  constructor(private http:HttpClient) {

  }

public login(username:string,password:string):Observable<any>{
    //backend

  this.http.post(this.ENDPOINT+"/login",{"username":username,"password":password}).subscribe({
    next:(res)=>{
      this.Data = res;
    }
  })

return of(this.Data);
}


public authenticateUser(appUser:AppUser):Observable<boolean>{
    this.authenticatedUser = appUser;
    localStorage.setItem("username",JSON.stringify(appUser.username));
    localStorage.setItem("access_token",JSON.stringify(appUser.token));
    localStorage.setItem("roles",JSON.stringify(appUser.roles));
    return of(true);
}

public hasRole(role:string):boolean{
    return this.authenticatedUser!.roles.includes(role);
}
public isAuthenticated(){
    return this.authenticatedUser!=undefined;
}

public logout():Observable<boolean>{
    this.authenticatedUser = undefined;
    localStorage.clear()
  return of(true);
}
}
