package com.nasrjb.digital_banking.repositories;

import com.nasrjb.digital_banking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
