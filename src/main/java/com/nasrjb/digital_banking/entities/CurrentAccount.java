package com.nasrjb.digital_banking.entities;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
//Just for SingleTable
@DiscriminatorValue("CC")
public class CurrentAccount extends BankAccount{
    private double overDraft;

}
