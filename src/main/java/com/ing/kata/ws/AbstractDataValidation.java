package com.ing.kata.ws;

import com.ing.kata.exception.BankException;

public abstract class AbstractDataValidation {



    protected void validateRefAccountOrThrowsException(String refAccount){
        if (refAccount.isBlank() || refAccount.length() > 25){
            throw new BankException("refAccount not correct.");
        }
    }
}
