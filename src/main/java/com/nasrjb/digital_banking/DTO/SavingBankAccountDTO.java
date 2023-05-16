package com.nasrjb.digital_banking.DTO;

import com.nasrjb.digital_banking.entities.BankAccount;
import com.nasrjb.digital_banking.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class SavingBankAccountDTO extends BankAccountDTO {
    private Long id;
    private double balance;
    private Date createAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;


}
