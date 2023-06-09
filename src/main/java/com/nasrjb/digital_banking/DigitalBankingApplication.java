package com.nasrjb.digital_banking;

import com.nasrjb.digital_banking.DTO.BankAccountDTO;
import com.nasrjb.digital_banking.DTO.CurrentBankAccountDTO;
import com.nasrjb.digital_banking.DTO.CustomerDTO;
import com.nasrjb.digital_banking.DTO.SavingBankAccountDTO;
import com.nasrjb.digital_banking.entities.BankAccountOperation;
import com.nasrjb.digital_banking.entities.CurrentAccount;
import com.nasrjb.digital_banking.entities.Customer;
import com.nasrjb.digital_banking.entities.SavingAccount;
import com.nasrjb.digital_banking.enums.AccountStatus;
import com.nasrjb.digital_banking.enums.OperationType;
import com.nasrjb.digital_banking.exceptions.BalanceNotSufficientException;
import com.nasrjb.digital_banking.exceptions.BankAccountNotFoundException;
import com.nasrjb.digital_banking.exceptions.CustomerNotFoundException;
import com.nasrjb.digital_banking.repositories.AccountOperationRepository;
import com.nasrjb.digital_banking.repositories.BankAccountRepository;
import com.nasrjb.digital_banking.repositories.CustomerRepository;
import com.nasrjb.digital_banking.security.entities.AppRole;
import com.nasrjb.digital_banking.security.entities.AppUser;
import com.nasrjb.digital_banking.security.repositories.AppRoleRepository;
import com.nasrjb.digital_banking.security.services.AccountService;
import com.nasrjb.digital_banking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService, AccountService accountService){

		return args -> {

			accountService.addNewRole(new AppRole(null,"CUSTOMER"));
			accountService.addNewRole(new AppRole(null,"ADMIN"));

			accountService.addNewUser(new AppUser(null,"Nasr","1234",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"Hamza","1234",new ArrayList<>()));
			accountService.addNewUser(new AppUser(null,"Admin","1234",new ArrayList<>()));

			accountService.addRoleToUser("Nasr","ADMIN");
			accountService.addRoleToUser("Nasr","CUSTOMER");
			accountService.addRoleToUser("Hamza","CUSTOMER");
			accountService.addRoleToUser("Admin","ADMIN");

			Stream.of("Nasr","Hajar","Omar").forEach(name->{
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			List<CustomerDTO> customerDTOS = bankAccountService.listCustomers();
			for(CustomerDTO customer:customerDTOS) {
				try {
					bankAccountService.saveCurrentBankAccount(Math.random()+90000,9000, customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()+82222,5.5, customer.getId());
					List<BankAccountDTO> bankAccountDTOS = bankAccountService.bankAccountList();
							for(BankAccountDTO bankAccount:bankAccountDTOS ) {
						for (int i = 0; i<10;i++){
							try {
								String accountID ;
								if (bankAccount instanceof SavingBankAccountDTO){
									accountID = ((SavingBankAccountDTO)bankAccount).getId();
								}else{
									accountID = ((CurrentBankAccountDTO)bankAccount).getId();
								}
								if (accountID != null) {
									bankAccountService.credit(accountID, 10000 + Math.random() + 12000, "Credit");
									bankAccountService.debit(accountID, 1000 + Math.random() + 1554, "Debit");
								}
							} catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
								throw new RuntimeException(e);
							}
						}
					};
				} catch (CustomerNotFoundException e) {
					throw new RuntimeException(e);
				}
			};
		};
	};


	CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager) {
		return args -> {

			jdbcUserDetailsManager.createUser(
					User.withUsername("Nasr").password(passwordEncoder().encode("1234")).roles("USER").build()
			);

				jdbcUserDetailsManager.createUser(
					User.withUsername("Hajar").password(passwordEncoder().encode("1234")).roles("USER").build()
			);
				jdbcUserDetailsManager.createUser(
					User.withUsername("Admin").password(passwordEncoder().encode("1234")).roles("ADMIN", "USER").build()
			);

		};
	}


	CommandLineRunner start
	(CustomerRepository customerRepository,
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
