package com.ing.kata;

import com.ing.kata.Entity.Account;
import com.ing.kata.Entity.Transaction;
import com.ing.kata.services.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class HistoryControlerTest {

    @Autowired
    private AccountService accountService;


    @PostConstruct
    void init() {
        Account account = accountService.findByRefOrThrowException("1");
        account.setBalance(new BigDecimal(0));
        accountService.save(account);
    }

    @Test
    void getHistoryTest() {
        Transaction transactionDeposit = Transaction.builder().amount(new BigDecimal(100)).refAccount("1").build();
        accountService.addDeposit(transactionDeposit);

        Transaction transactionWithdrawal = Transaction.builder().amount(new BigDecimal(50)).refAccount("1").build();
        accountService.doWhithdrawal(transactionWithdrawal);

        Assertions.assertEquals(2, accountService.getOperationsHistory("1").size());
    }


}
