package com.ing.kata.services;

import com.ing.kata.Entity.Account;
import com.ing.kata.Entity.Client;
import com.ing.kata.Entity.Transaction;
import com.ing.kata.exception.BankException;
import com.ing.kata.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class AccountService implements AccountInterface {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @PostConstruct
    public void init() {
        clientService.save(Client.builder().account(accountRepository.save(Account.builder().refAccount("1").build())).build());
    }

    public List<Transaction> getOperationsHistory(String refAccount){
        findByRefOrInterrupt(refAccount);
        return transactionService.findAllByAccountId(refAccount);
    }

    public int getBalance(String refAccount){
        Account account = findByRefOrInterrupt(refAccount);
        return account.getBalance();
    }


    public void addDeposit(Transaction transaction) {
        validateDepositData(transaction.getAmount());
        Account account = findByRefOrInterrupt(transaction.getRefAccount());
        account.setBalance(account.getBalance() + transaction.getAmount());
        saveData(transaction, account);

    }

    public void doWhithdrawal(Transaction transaction){
        Account account = findByRefOrInterrupt(transaction.getRefAccount());
        validateWithdrawallData(transaction, account);
        account.setBalance(account.getBalance() - transaction.getAmount());
        saveData(transaction, account);
    }

    public Account findByRefOrInterrupt(String refAccount) {
        return accountRepository.findAccountByRefAccount(refAccount).orElseThrow(() -> new BankException("Account not found"));
    }

    public Account save(Account account){
        return accountRepository.save(account);
    }

    private void validateDepositData(int amount) {
        if(amount < 0.01d){
            throw new BankException("Amount not correct.");
        }
    }

    private void validateWithdrawallData(Transaction transaction, Account account) {
        if(account.getBalance() <= transaction.getAmount()){
            throw new BankException("insufficient account provision.");
        }
    }

    private void saveData(Transaction transaction, Account account) {
        save(account);
        transactionService.save(transaction);
    }

}
