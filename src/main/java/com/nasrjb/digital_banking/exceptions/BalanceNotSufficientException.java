package com.nasrjb.digital_banking.exceptions;

public class BalanceNotSufficientException extends Exception {
    public BalanceNotSufficientException(String message){
        super(message);
    }
}
