package com.nasrjb.digital_banking.security.repositories;

import com.nasrjb.digital_banking.security.entities.AppRole;
import com.nasrjb.digital_banking.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(String username);
}
