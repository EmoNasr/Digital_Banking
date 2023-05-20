package com.nasrjb.digital_banking.security.repositories;

import com.nasrjb.digital_banking.security.entities.AppRole;
import com.nasrjb.digital_banking.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {
    AppRole findByRoleName(String roleName);
}
