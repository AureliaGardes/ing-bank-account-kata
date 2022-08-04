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
public class HistoryControler extends AbstractDataValidation {

    @Autowired
    private AccountService accountService;


    @GetMapping(path = "/client/history/account/{refAccount}")
    public ResponseEntity<List<Transaction>> getHistoryFromAccount(@PathVariable("refAccount") String refAccount) {
        try {
            validateRefAccountOrThrowsException(refAccount);
            List<Transaction> history = accountService.getOperationsHistory(refAccount);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (BankException be) {
            return new ResponseEntity(be.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
    }


}
