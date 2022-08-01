package com.ing.kata.services;

import com.ing.kata.Entity.Transaction;
import com.ing.kata.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> findAllByAccountId(String accountId){
        return transactionRepository.findAllByRefAccount(accountId);
    }

    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }
}
