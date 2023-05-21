import { Injectable } from '@angular/core';
import {AppUser} from "../model/User-model";
import {Observable, of, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  users:AppUser[]=[];
  public authenticatedUser: AppUser|undefined;
  constructor() {
    this.users.push({userID:"id3",username:"user2",password:"password",role:["USER"]});
    this.users.push({userID:"id2",username:"admin",password:"password",role:["USER","ADMIN"]});
  }

public login(username:string,password:string):Observable<AppUser>{
    //backend
  let appUser = this.users.find(u=>u.username==username);
if (!appUser)
  return throwError(()=>new Error("User not found"));
if(appUser.password!=password){
  return throwError(()=>new Error("Bad Credentials"));
}
return of(appUser);
}


public authenticateUser(appUser:AppUser):Observable<boolean>{
    this.authenticatedUser = appUser;
    localStorage.setItem("authUser",JSON.stringify({username:appUser.username,roles:appUser.role,jwt:"JWT_TOKEN"}));
    return of(true);
}

public hasRole(role:string):boolean{
    return this.authenticatedUser!.role.includes(role);
}
public isAuthenticated(){
    return this.authenticatedUser!=undefined;
}

public logout():Observable<boolean>{
    this.authenticatedUser = undefined;
    localStorage.removeItem("authUser")
  return of(true);
}
}
