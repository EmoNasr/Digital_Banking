package com.nasrjb.digital_banking.DTO;
import com.nasrjb.digital_banking.enums.OperationType;
import lombok.Data;


import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private String description;
    private OperationType type;

}
