import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{
  userFormGroup!:FormGroup;

  constructor(private fb:FormBuilder,private router:Router) {
  }
  ngOnInit(): void {
    this.userFormGroup = this.fb.group(
      {
        username:this.fb.control(""),
        password:this.fb.control("")
      }
    )
  }


  handelLogin() {

  }
}
