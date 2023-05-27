package com.nasrjb.digital_banking.security.web;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
