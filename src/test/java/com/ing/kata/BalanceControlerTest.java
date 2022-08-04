package com.ing.kata;

import com.ing.kata.Entity.Account;
import com.ing.kata.Entity.Transaction;
import com.ing.kata.services.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@SpringBootTest
class BalanceControlerTest {

    @Autowired
    private AccountService accountService;


    @PostConstruct
    void init() {
        Account account = accountService.findByRefOrThrowException("1");
        account.setBalance(new BigDecimal(0));
        accountService.save(account);
    }

    @Test
    void getBalanceTest() {
        Transaction transaction = Transaction.builder().amount(new BigDecimal(100)).refAccount("1").build();

        accountService.addDeposit(transaction);

        accountService.getBalance("1");
        Assertions.assertEquals(0, accountService.getBalance("1").compareTo(new BigDecimal(100)));
    }


}
