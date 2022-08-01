package com.ing.kata.services;

import com.ing.kata.Entity.Transaction;

import java.util.List;

public interface AccountInterface {

    List<Transaction> getOperationsHistory(String refAccount);

    int getBalance(String refAccount);

    void addDeposit(Transaction transaction);

    void doWhithdrawal(Transaction transaction);
}
