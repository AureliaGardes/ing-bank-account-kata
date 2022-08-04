package com.ing.kata.ws;

import com.ing.kata.Entity.Transaction;
import com.ing.kata.exception.BankException;
import com.ing.kata.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static java.lang.Float.valueOf;

@Slf4j
@RestController
public class DepositControler extends AbstractDataValidation{

    private static final float MINIMUM = 0.01f;
    @Autowired
    private AccountService accountService;

    @PutMapping(path = "/client/account/transaction/deposit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> doDeposit(@RequestBody Transaction transaction) {
        try {
            validateDepositDataOrThrowException(transaction);
            log.info("Receipt of a transaction with account ref : " + transaction.getRefAccount());
            accountService.addDeposit(transaction);
            log.info("transaction successfully processed");
        } catch (BankException be) {
            return new ResponseEntity<>(be.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Operation successfully processed.", HttpStatus.OK);
    }

    private void validateDepositDataOrThrowException(Transaction transaction){
        validateRefAccountOrThrowsException(transaction.getRefAccount());
        validateDepositAmount(transaction.getAmount());
    }

    private void validateDepositAmount(BigDecimal amount){
        if(amount.compareTo(new BigDecimal(MINIMUM)) < 0){
            throw new BankException("Amount not correct.");
        }
    }

}
