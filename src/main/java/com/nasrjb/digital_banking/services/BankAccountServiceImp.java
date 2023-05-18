package com.nasrjb.digital_banking.services;

import com.nasrjb.digital_banking.DTO.*;
import com.nasrjb.digital_banking.entities.*;
import com.nasrjb.digital_banking.enums.OperationType;
import com.nasrjb.digital_banking.exceptions.BalanceNotSufficientException;
import com.nasrjb.digital_banking.exceptions.BankAccountNotFoundException;
import com.nasrjb.digital_banking.exceptions.CustomerNotFoundException;
import com.nasrjb.digital_banking.mappers.BankAccountMapperImpl;
import com.nasrjb.digital_banking.repositories.AccountOperationRepository;
import com.nasrjb.digital_banking.repositories.BankAccountRepository;
import com.nasrjb.digital_banking.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor //Dependency injection with constructor
@Slf4j //=>
//Logger log = LoggerFactory.getLogger(this.getClass().getName());
public class BankAccountServiceImp implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    private BankAccountMapperImpl bankAccountDTOMapper;
    //For logfile
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = bankAccountDTOMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountDTOMapper.fromCustomer(savedCustomer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = bankAccountDTOMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountDTOMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting new Customer "+id);
        customerRepository.deleteById(id);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        CurrentAccount bankAccount;
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");

        bankAccount=new CurrentAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(initialBalance);
        bankAccount.setCustomer(customer);
        bankAccount.setOverDraft(overDraft);
        CurrentAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        return bankAccountDTOMapper.fromCurrentBankAccount(savedBankAccount);
    }


    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double inerestRate, Long customerId) throws CustomerNotFoundException {
        SavingAccount bankAccount;
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");

        bankAccount=new SavingAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreateAt(new Date());
        bankAccount.setBalance(initialBalance);
        bankAccount.setCustomer(customer);
        bankAccount.setInterestRate(inerestRate);
        SavingAccount savedSavingBankAccount = bankAccountRepository.save(bankAccount);

        return bankAccountDTOMapper.fromSavingBankAccount(savedSavingBankAccount);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> bankAccountDTOMapper
                        .fromCustomer(customer))
                .collect(Collectors.toList());
        return customerDTOS;
    }

    @Override

    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));

        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return bankAccountDTOMapper.fromSavingBankAccount(savingAccount);
        }else{
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return bankAccountDTOMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }

        BankAccountOperation accountOperation = new BankAccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAccount(bankAccount);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);

        log.info(bankAccount.getId()+"has been debited by this amount: "+amount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank account not found"));
        BankAccountOperation accountOperation = new BankAccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setAccount(bankAccount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);

        log.info(bankAccount.getId()+"has been credited by this amount: "+amount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource);
        log.info("Transfer from "+accountIdSource+" to "+accountIdDestination);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccount = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccount.stream().map(bankaccount1 -> {
            if (bankaccount1 instanceof SavingAccount) {
                SavingAccount account = (SavingAccount) bankaccount1;
                return bankAccountDTOMapper.fromSavingBankAccount(account);
            } else {
                CurrentAccount account = (CurrentAccount) bankaccount1;
                return bankAccountDTOMapper.fromCurrentBankAccount(account);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("Customer Not Found !")
        );
        return bankAccountDTOMapper.fromCustomer(customer);
    }

    public List<AccountOperationDTO> accountHistory(String accountID){
            List<BankAccountOperation> accountOperation = accountOperationRepository.findByAccount_Id(accountID);
            return accountOperation.stream().map(op->bankAccountDTOMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw new BankAccountNotFoundException("Bank Not found");
        Page<BankAccountOperation> accountOperations = accountOperationRepository.findByAccount_Id(accountId, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> operationDTOS = accountOperations.getContent().stream().map(op -> bankAccountDTOMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOs(operationDTOS);
        accountHistoryDTO.setAccountId(accountId);
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountHistoryDTO.getTotalPages());

        return accountHistoryDTO;
    }
}
