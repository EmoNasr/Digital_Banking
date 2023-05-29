import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AccountService} from "../services/account.service";

import {HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AuthenticationService} from "../services/authentication.service";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit{

  accountFormGroup!:FormGroup;
  currentPage:number=0;
  pagesize:number=5;
  account$!:Observable<any>;
  operationFormGroup!:FormGroup;

  constructor(public auth:AuthenticationService,private fb:FormBuilder,private accountService:AccountService) {
  }
  ngOnInit(): void {
    this.accountFormGroup=this.fb.group(
      {
        accountId:['',Validators.required]
      }
    )
    this.operationFormGroup = this.fb.group(
      {
        operationType : this.fb.control(null),
        amountOperation:this.fb.control(0),
        description:this.fb.control(null),
        accountDestination:this.fb.control(null),
        accountSource:this.fb.control(null)
      }
    )


    //this.handelSearchAccount();
    this.handelLoadingAccount();
  }

  handelSearchAccount() {
    let accountId = this.accountFormGroup.value.accountId;
    this.account$ = this.accountService.getAccount(accountId,this.currentPage,this.pagesize);
    this.account$.subscribe(data=>{
    })
  }

  handelLoadingAccount(){
    this.account$=this.accountService.getAccountsId(this.currentPage,this.pagesize);
   this.account$.subscribe(data=>{

    });
  }

  handelAccountOperations() {
    let accountID:string=this.operationFormGroup.value.accountSource;
    let operationType = this.operationFormGroup.value.operationType;

    switch (operationType){
      case 'DEBIT':
        this.accountService.debit(accountID
          ,this.operationFormGroup.value.amountOperation
          ,this.operationFormGroup.value.description)
          .subscribe(
            {
              next:(date)=>{
                alert("Success Debit");
                this.operationFormGroup.reset();

                this.handelLoadingAccount();
              },
              error:(err)=> {


                this.handelLoadingAccount();
              }
            }
          );
        break;
      case 'CREDIT':
        this.accountService.credit(accountID
          ,this.operationFormGroup.value.amountOperation
          ,this.operationFormGroup.value.description)
          .subscribe(
            {
              next:(date)=>{
                alert("Success Credit");
                this.operationFormGroup.reset();
                this.handelLoadingAccount();
              },
              error:(err)=> {

                this.handelLoadingAccount();
              }
            });
        break;
      case 'TRANSFER':
        this.accountService.transfer(
            accountID
          ,this.operationFormGroup.value.accountDestination
          ,this.operationFormGroup.value.amountOperation
          ,this.operationFormGroup.value.description)
          .subscribe(
            {
              next:(date)=>{
                alert("Success Transfer");
                this.operationFormGroup.reset();
                this.handelLoadingAccount();
              },
              error:(err)=> {

                this.handelLoadingAccount();
              }
            });
        break;

    }


  }

  goto(page: number) {
    this.currentPage=page;

    if(this.accountFormGroup.value.accountId == ''){
      this.handelLoadingAccount()
    }else{

      this.handelSearchAccount();
    }

  }
}
