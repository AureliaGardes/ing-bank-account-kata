package com.ing.kata.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

@Setter
@Getter

public class BankException extends RuntimeException {

    private String errorCode;

    public BankException(String errorCode) {
        Assert.notNull(errorCode, "Error Code not defined");
        this.errorCode=errorCode;
    }
}
