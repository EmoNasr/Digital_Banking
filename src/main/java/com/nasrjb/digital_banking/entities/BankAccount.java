package com.nasrjb.digital_banking.entities;

import com.nasrjb.digital_banking.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// if we use this strategy we need to make this class abstract because we don't need to create it's in the database

//Just for SingleTable
@DiscriminatorColumn(name = "TYPE",length = 4,discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date createAt;
    private AccountStatus status;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "account",cascade = CascadeType.REMOVE)
    List<BankAccountOperation> operations;

}
