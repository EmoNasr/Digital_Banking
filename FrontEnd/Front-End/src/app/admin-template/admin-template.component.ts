import { Component } from '@angular/core';
import {AuthenticationService} from "../services/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-template',
  templateUrl: './admin-template.component.html',
  styleUrls: ['./admin-template.component.scss']
})
export class AdminTemplateComponent {
  constructor(public auth:AuthenticationService,private router:Router) {
  }

  public handelLogout() {
    this.auth.logout().subscribe({
      next:(data)=>
      {
        this.router.navigateByUrl("/login");
      }
    });
  }
}
