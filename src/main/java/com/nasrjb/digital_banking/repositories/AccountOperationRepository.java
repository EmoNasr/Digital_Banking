package com.nasrjb.digital_banking.repositories;

import com.nasrjb.digital_banking.entities.BankAccount;
import com.nasrjb.digital_banking.entities.BankAccountOperation;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<BankAccountOperation,Long> {
     List<BankAccountOperation> findByAccount_Id(String accountId);
     Page<BankAccountOperation> findByAccount_Id(String accounId, PageRequest pageRequest);
}
