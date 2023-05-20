package com.nasrjb.digital_banking.security.web;

import com.nasrjb.digital_banking.security.entities.AppRole;
import com.nasrjb.digital_banking.security.entities.AppUser;
import com.nasrjb.digital_banking.security.services.AccountService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccountRestController {
    private AccountService accountService;

    @GetMapping("/users")
    public List<AppUser> appUsers(){
        return accountService.listUsers();
    };

    @PostMapping("/users")
    public AppUser saveUser(@RequestBody AppUser appUser){
        return accountService.addNewUser(appUser);
    }
    @PostMapping("/roles")
    public AppRole saveRole(@RequestBody AppRole appRole){
        return accountService.addNewRole(appRole);
    }

    @PostMapping("/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUSerForm roleUSerForm){
         accountService.addRoleToUser(roleUSerForm.getUsername(),roleUSerForm.getRoleName());
    }
}

@Data
class RoleUSerForm{
    private String username;
    private String RoleName;
}
