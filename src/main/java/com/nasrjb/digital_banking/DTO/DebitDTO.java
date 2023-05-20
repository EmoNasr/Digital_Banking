package com.nasrjb.digital_banking.DTO;

import lombok.Data;

@Data
public class DebitDTO {
    private String accountID;
    private double amount;
    private String description;
}
