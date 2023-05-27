package com.nasrjb.digital_banking.security.web;

import com.nasrjb.digital_banking.security.entities.AppRole;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Builder
public class AuthenticationResponse {
    private Collection<String> roles;
    private String username;
    private String token;
}
