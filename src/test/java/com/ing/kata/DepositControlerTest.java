package com.ing.kata;

import com.ing.kata.Entity.Account;
import com.ing.kata.Entity.Transaction;
import com.ing.kata.services.AccountService;
import com.ing.kata.ws.DepositControler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@SpringBootTest
class DepositControlerTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private DepositControler depositControler;


    @PostConstruct
    void init() {
        Account account = accountService.findByRefOrThrowException("1");
        account.setBalance(new BigDecimal(0));
        accountService.save(account);
    }

    @Test
    void doTransactionDepositOKTest() {
        Transaction transaction = Transaction.builder().amount(new BigDecimal(100)).refAccount("1").build();

        depositControler.doDeposit(transaction);

        Account account = accountService.findByRefOrThrowException(transaction.getRefAccount());
        Assertions.assertEquals(0, account.getBalance().compareTo(transaction.getAmount()));
    }

    @Test
    void doTransactionDepositKOTest() {
        Transaction transaction = Transaction.builder().amount(new BigDecimal(0)).refAccount("1").build();

        ResponseEntity responseEntity = depositControler.doDeposit(transaction);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Account account = accountService.findByRefOrThrowException(transaction.getRefAccount());
        Assertions.assertEquals(0, account.getBalance().intValue());
    }
}
