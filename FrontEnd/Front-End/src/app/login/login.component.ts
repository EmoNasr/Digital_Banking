import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../services/authentication.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  userFormGroup!: FormGroup;

  erreur !:string;
  constructor(private fb: FormBuilder, private router: Router, private auth: AuthenticationService) {
  }

  ngOnInit(): void {
    this.userFormGroup = this.fb.group(
      {
        username: this.fb.control(""),
        password: this.fb.control("")
      }
    )
  }


  handelLogin() {
    let username = this.userFormGroup.value.username;
    let password = this.userFormGroup.value.password;
    this.auth.login(username, password).subscribe(
      {
        next: (appUser) => {
            console.log(appUser);
            this.auth.authenticateUser(appUser).subscribe(
              {
                next:(data)=>{
                  this.router.navigateByUrl("/admin");
                },
                error: (err) => {
                  console.log(err.message)
                }
              }
            )
        },
        error: (err) => {
          this.erreur = err.message;
          console.log(err.message)
        }
      });
  }
}
