package com.ing.kata.services;

import com.ing.kata.Entity.Account;
import com.ing.kata.Entity.Client;
import com.ing.kata.Entity.Transaction;
import com.ing.kata.exception.BankException;
import com.ing.kata.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @PostConstruct
    public void init() {
        accountRepository.save(Account.builder().refAccount("1").balance(new BigDecimal(0)).build());
    }

    public List<Transaction> getOperationsHistory(String refAccount){
        findByRefOrThrowException(refAccount);
        return transactionService.findAllByAccountId(refAccount);
    }

    public BigDecimal getBalance(String refAccount){
        Account account = findByRefOrThrowException(refAccount);
        return account.getBalance();
    }


    public void addDeposit(Transaction transaction) {
        Account account = findByRefOrThrowException(transaction.getRefAccount());
        account.setBalance(account.getBalance().add(transaction.getAmount()));
        saveData(transaction, account);

    }

    public void doWhithdrawal(Transaction transaction){
        Account account = findByRefOrThrowException(transaction.getRefAccount());
        validateWithdrawallData(transaction, account);
        account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        saveData(transaction, account);
    }

    public Account findByRefOrThrowException(String refAccount) {
        return accountRepository.findAccountByRefAccount(refAccount).orElseThrow(() -> new BankException("Account not found"));
    }

    public Account save(Account account){
        return accountRepository.save(account);
    }

    private void validateWithdrawallData(Transaction transaction, Account account) {
        if(account.getBalance().compareTo(transaction.getAmount()) < 0){
            throw new BankException("insufficient account provision.");
        }
    }

    private void saveData(Transaction transaction, Account account) {
        save(account);
        transactionService.save(transaction);
    }
}
