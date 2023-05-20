import { Injectable } from '@angular/core';
import {AppUser} from "./model/User-model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  users!:AppUser[];
  constructor() {
    this.users.push({userID:"id1",username:"user1",password:"password",role:["USER"]});
    this.users.push({userID:"id3",username:"user2",password:"password",role:["USER"]});
    this.users.push({userID:"id2",username:"admin",password:"password",role:["USER","ADMIN"]});
  }


}
