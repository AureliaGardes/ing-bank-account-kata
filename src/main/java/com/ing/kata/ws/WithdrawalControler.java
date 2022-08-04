package com.ing.kata.ws;

import com.ing.kata.Entity.Account;
import com.ing.kata.Entity.Transaction;
import com.ing.kata.exception.BankException;
import com.ing.kata.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WithdrawalControler extends AbstractDataValidation {

    @Autowired
    private AccountService accountService;

    @PutMapping(path = "/client/account/transaction", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> doWithdrawal(@RequestBody Transaction transaction) {
        try {
            validateWithdrawalDataOrThrowException(transaction);
            log.info("Receipt of a transaction with account ref : " + transaction.getRefAccount());
            accountService.doWhithdrawal(transaction);
            log.info("transaction successfully processed");
        } catch (BankException be) {
            return new ResponseEntity<>(be.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Operation successfully processed.", HttpStatus.OK);
    }

    private void validateWithdrawalDataOrThrowException(Transaction transaction) {
        validateRefAccountOrThrowsException(transaction.getRefAccount());
        Account account = accountService.findByRefOrThrowException(transaction.getRefAccount());
        if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new BankException("insufficient account provision.");
        }
    }

}
