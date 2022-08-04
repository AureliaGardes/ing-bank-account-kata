package com.ing.kata.ws;

import com.ing.kata.exception.BankException;
import com.ing.kata.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
public class BalanceControler extends AbstractDataValidation{

    @Autowired
    private AccountService accountService;


    @GetMapping(path = "/client/balance/account/{refAccount}")
    public ResponseEntity<String> getBalanceFromAccount(@PathVariable("refAccount") String refAccount) {
        try {
            validateRefAccountOrThrowsException(refAccount);
            BigDecimal balance = accountService.getBalance(refAccount);
            return new ResponseEntity<>("Balance of account : " + balance, HttpStatus.OK);
        } catch (BankException be) {
            return new ResponseEntity<>(be.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
    }


}
