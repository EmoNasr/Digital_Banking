package com.nasrjb.digital_banking.repositories;

import com.nasrjb.digital_banking.entities.BankAccount;
import com.nasrjb.digital_banking.entities.BankAccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<BankAccountOperation,Long> {
}
