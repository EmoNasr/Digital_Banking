package com.nasrjb.digital_banking.web;

import com.nasrjb.digital_banking.DTO.*;
import com.nasrjb.digital_banking.dtos.CreditDTO;
import com.nasrjb.digital_banking.entities.BankAccountOperation;
import com.nasrjb.digital_banking.exceptions.BalanceNotSufficientException;
import com.nasrjb.digital_banking.exceptions.BankAccountNotFoundException;
import com.nasrjb.digital_banking.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }
    @PostMapping("/accountDetails")
    public AccountHistoryDTO getBankAccountDetail(@RequestBody CustomerDTO customerName, @RequestParam(name = "page",defaultValue = "0") int page,
                                                  @RequestParam(name = "size",defaultValue = "5")int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistoryByCustomer(customerName.getName(),page,size);
    }
@GetMapping("/accountHistory/{name}")
public AccountHistoryDTO getAccountHistory(@PathVariable String name, @RequestParam(name = "page",defaultValue = "0") int page,
                                                    @RequestParam(name = "size",defaultValue = "5")int size){
        return  bankAccountService.accountHistory(name,page,size);
}
    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getHistory(@PathVariable String accountId,
                                              @RequestParam(name = "page",defaultValue = "0") int page,
                                              @RequestParam(name = "size",defaultValue = "5")int size) throws BankAccountNotFoundException {

        return bankAccountService.getAccountHistory(accountId,page,size);
    }
    @GetMapping("/accounts/pageOperations")
    public List<AccountHistoryDTO> getAccount(@RequestParam(name = "page",defaultValue = "0") int page,
                                              @RequestParam(name = "size",defaultValue = "5")int size) throws BankAccountNotFoundException {

        return bankAccountService.getAccountsIds(page,size);
    }

    @PostMapping("/accounts/debit")
    public DebitDTO debitDTO(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountID(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }
    @PostMapping("/accounts/credit")
    public CreditDTO creditDTO(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountID(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    public void transferRequestDTO(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount()
        );
    }

}
