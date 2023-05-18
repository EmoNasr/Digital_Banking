package com.nasrjb.digital_banking.DTO;

import com.nasrjb.digital_banking.entities.BankAccountOperation;
import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOs;
}
