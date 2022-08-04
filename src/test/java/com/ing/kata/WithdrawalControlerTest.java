package com.ing.kata;

import com.ing.kata.Entity.Account;
import com.ing.kata.Entity.Transaction;
import com.ing.kata.services.AccountService;
import com.ing.kata.ws.WithdrawalControler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@SpringBootTest
class WithdrawalControlerTest {

    @Autowired
    private WithdrawalControler withdrawalControler;

    @Autowired
    private AccountService accountService;


    @PostConstruct
    void init() {
        Account account = accountService.findByRefOrThrowException("1");
        account.setBalance(new BigDecimal(0));
        accountService.save(account);
    }

    @Test
    void doTransactionWithdrawalKOTest() {
        Transaction transaction = Transaction.builder().amount(new BigDecimal(100)).refAccount("1").build();

        ResponseEntity<String> responseEntity = withdrawalControler.doWithdrawal(transaction);

        Account account = accountService.findByRefOrThrowException(transaction.getRefAccount());

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals(0, account.getBalance().intValue());
    }

    @Test
    void doTransactionWithdrawalOKTest() {
        Account account = accountService.findByRefOrThrowException("1");
        account.setBalance(new BigDecimal(200));
        accountService.save(account);

        Transaction transaction = Transaction.builder().amount(new BigDecimal(100)).refAccount("1").build();

        ResponseEntity<String> responseEntity = withdrawalControler.doWithdrawal(transaction);
        Account accountUpdated = accountService.findByRefOrThrowException("1");
        Assertions.assertEquals(0, accountUpdated.getBalance().compareTo(new BigDecimal(100)));
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
