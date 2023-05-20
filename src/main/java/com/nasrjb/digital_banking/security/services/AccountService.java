package com.nasrjb.digital_banking.security.services;

import com.nasrjb.digital_banking.security.entities.AppRole;
import com.nasrjb.digital_banking.security.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username, String roleName);
    AppUser loadUserByUserName(String username);
    List<AppUser> listUsers();
}
