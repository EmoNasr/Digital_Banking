package com.nasrjb.digital_banking;

import com.nasrjb.digital_banking.entities.BankAccountOperation;
import com.nasrjb.digital_banking.entities.CurrentAccount;
import com.nasrjb.digital_banking.entities.Customer;
import com.nasrjb.digital_banking.entities.SavingAccount;
import com.nasrjb.digital_banking.enums.AccountStatus;
import com.nasrjb.digital_banking.enums.OperationType;
import com.nasrjb.digital_banking.repositories.AccountOperationRepository;
import com.nasrjb.digital_banking.repositories.BankAccountRepository;
import com.nasrjb.digital_banking.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){

		return args -> {
			Stream.of("Hamza","Nasr","Hajar").forEach(name->{
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});


			//For each customer we create a saving account and current account
			for(Customer customer:customerRepository.findAll()) {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*90000);
				currentAccount.setCreateAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(customer);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingaccount = new SavingAccount();
				savingaccount.setId(UUID.randomUUID().toString());
				savingaccount.setBalance(Math.random()*90000);
				savingaccount.setCreateAt(new Date());
				savingaccount.setStatus(AccountStatus.CREATED);
				savingaccount.setCustomer(customer);
				savingaccount.setInterestRate(5);
				bankAccountRepository.save(savingaccount);
			};

			bankAccountRepository.findAll().forEach(bankAccount -> {
				for(int i = 0; i < 5; i++){
					BankAccountOperation operationRepository = new BankAccountOperation();
					operationRepository.setOperationDate(new Date());
					operationRepository.setAmount(Math.random()*12220);
					operationRepository.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
					operationRepository.setAccount(bankAccount);
					accountOperationRepository.save(operationRepository);
				}
			});
		};
	}

}
