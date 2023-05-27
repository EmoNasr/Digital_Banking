import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AccountService} from "../services/account.service";
import {AccountDetails} from "../model/account.model";
import {Observable} from "rxjs";
import {HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit{

  accountFormGroup!:FormGroup;
  currentPage:number=1;
  pagesize:number=5;
  account$!:Observable<AccountDetails>;
  operationFormGroup!:FormGroup;
  private token!: string | null ;
  private headers_object!: HttpHeaders;
  constructor(private fb:FormBuilder,private accountService:AccountService) {
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
        accountDestination:this.fb.control(null)
      }
    )
    this.token =  localStorage.getItem("access_token");
    this.headers_object = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': "Bearer "+this.token})

    //this.handelSearchAccount();
  }

  handelSearchAccount() {
    let accountId = this.accountFormGroup.value.accountId;
    console.log(accountId)
    this.account$ = this.accountService.getAccount(accountId,this.currentPage,this.pagesize,this.headers_object);
    this.account$.subscribe(data=>{
    })
  }

  handelAccountOperations() {
    let accountID:string=this.accountFormGroup.value.accountId;
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

                this.handelSearchAccount();
              },
              error:(err)=> {
                console.log(err);

                this.handelSearchAccount();
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
                this.handelSearchAccount();
              },
              error:(err)=> {
                console.log(err);
                this.handelSearchAccount();
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
                this.handelSearchAccount();
              },
              error:(err)=> {
                console.log(err);
                this.handelSearchAccount();
              }
            });
        break;

    }


  }

  goto(page: number) {
    this.currentPage=page;
    this.handelSearchAccount();

  }
}
