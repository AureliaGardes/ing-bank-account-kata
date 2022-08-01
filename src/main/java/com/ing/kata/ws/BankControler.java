package com.ing.kata.ws;

import com.ing.kata.Entity.Transaction;
import com.ing.kata.exception.BankException;
import com.ing.kata.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class BankControler {

    @Autowired
    private AccountService accountService;

    @PutMapping(path = "/client/account/transaction", consumes = "application/json", produces = "application/json")
    public ResponseEntity doTransaction(@RequestBody Transaction transaction) {
        log.info("Receipt of a transaction with account ref : " + transaction.getRefAccount());
        try {
            switch (transaction.getType()) {
                case DEPOSIT:
                    accountService.addDeposit(transaction);
                    log.info("transaction successfully processed");
                    break;
                case WITHDRAWAL:
                    accountService.doWhithdrawal(transaction);
                    log.info("transaction successfully processed");
                    break;
            }
        } catch (BankException be) {
            return new ResponseEntity(be.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity("Operation successfully processed.", HttpStatus.OK);
    }

    @GetMapping(path = "/client/account/{refAccount}")
    public ResponseEntity<List<Transaction>> getHistoryFromAccount(@PathVariable("refAccount") String refAccount) {
        try {
            List<Transaction> history = accountService.getOperationsHistory(refAccount);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (BankException be) {
            return new ResponseEntity(be.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/client/account/balance/{refAccount}")
    public ResponseEntity getBalanceFromAccount(@PathVariable("refAccount") String refAccount) {
        try {
            int balance = accountService.getBalance(refAccount);
            return new ResponseEntity("Balance of account : " + balance, HttpStatus.OK);
        } catch (BankException be) {
            return new ResponseEntity(be.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
    }


}
