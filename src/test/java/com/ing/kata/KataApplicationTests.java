package com.ing.kata;

import com.ing.kata.Entity.Account;
import com.ing.kata.Entity.Transaction;
import com.ing.kata.Entity.Type;
import com.ing.kata.exception.BankException;
import com.ing.kata.services.AccountService;
import com.ing.kata.services.ClientService;
import com.ing.kata.ws.BankControler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class KataApplicationTests {

    @Autowired
    private AccountService accountService;


    @PostConstruct
    void init() {
        Account account = accountService.findByRefOrInterrupt("1");
        account.setBalance(0);
        accountService.save(account);
    }

    @Test
    void doTransactionDepositOKTest() {
        Transaction transaction = Transaction.builder().amount(100).refAccount("1").type(Type.DEPOSIT).build();

        accountService.addDeposit(transaction);

        Account account = accountService.findByRefOrInterrupt(transaction.getRefAccount());
        Assertions.assertEquals(account.getBalance(), transaction.getAmount());
    }

	@Test
	void doTransactionDepositKOTest() {
		Transaction transaction = Transaction.builder().amount(0).refAccount("1").type(Type.DEPOSIT).build();


        BankException thrown = Assertions.assertThrows(BankException.class, () -> {
            accountService.addDeposit(transaction);
        });
        Assertions.assertEquals("Amount not correct.", thrown.getErrorCode());
	}


    @Test
    void doTransactionWithdrawalKOTest() {
        Transaction transaction = Transaction.builder().amount(100).refAccount("1").type(Type.WITHDRAWAL).build();
        accountService.findByRefOrInterrupt(transaction.getRefAccount());

        BankException thrown = Assertions.assertThrows(BankException.class, () -> {
            accountService.doWhithdrawal(transaction);
        });
        Assertions.assertEquals("insufficient account provision.", thrown.getErrorCode());
    }

    @Test
    void doTransactionWithdrawalOKTest() {
        Account account = accountService.findByRefOrInterrupt("1");
        account.setBalance(200);
        accountService.save(account);

        Transaction transaction = Transaction.builder().amount(100).refAccount("1").type(Type.WITHDRAWAL).build();
        accountService.findByRefOrInterrupt(transaction.getRefAccount());


            accountService.doWhithdrawal(transaction);
        Account accountUpdated = accountService.findByRefOrInterrupt("1");
        Assertions.assertEquals(100, accountUpdated.getBalance());
    }

    @Test
    void getBalanceTest() {
        Transaction transaction = Transaction.builder().amount(100).refAccount("1").type(Type.WITHDRAWAL).build();

        accountService.addDeposit(transaction);

        accountService.getBalance("1");
        Assertions.assertEquals(100, accountService.getBalance("1"));
    }

    @Test
    void getHistoryTest() {
        Transaction transactionDeposit = Transaction.builder().amount(100).refAccount("1").type(Type.WITHDRAWAL).build();
        accountService.addDeposit(transactionDeposit);

        Transaction transactionWithdrawal = Transaction.builder().amount(50).refAccount("1").type(Type.WITHDRAWAL).build();
        accountService.doWhithdrawal(transactionWithdrawal);

        Assertions.assertEquals(2, accountService.getOperationsHistory("1").size());
    }


}
