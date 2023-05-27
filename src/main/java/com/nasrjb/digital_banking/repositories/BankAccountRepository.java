package com.nasrjb.digital_banking.repositories;

import com.nasrjb.digital_banking.entities.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    BankAccount findByCustomerId(Long id);

}
