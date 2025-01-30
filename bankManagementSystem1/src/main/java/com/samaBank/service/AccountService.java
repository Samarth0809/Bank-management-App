package com.samaBank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samaBank.model.Account;
import com.samaBank.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }
    
    @Transactional
    public void deleteAccountById(Long id) {
        // Fetch the account to get its number before deletion
        Account accountToDelete = accountRepository.findById(id).orElse(null);
        if (accountToDelete != null) {
            Long accountNumber = accountToDelete.getAccountNumber();
            // Delete the account
            accountRepository.deleteById(id);
            // Decrement the account numbers of all accounts with higher numbers
            decrementAccountNumbers(accountNumber);
        } else {
            throw new RuntimeException("Account not found with ID: " + id);
        }
    }

    private void decrementAccountNumbers(Long accountNumber) {
        List<Account> accounts = accountRepository.findByAccountNumberGreaterThan(accountNumber);
        for (Account account : accounts) {
            account.setAccountNumber(account.getAccountNumber() - 1);
            accountRepository.save(account);
        }
    }

    public Account createAccount(String owner, double balance) {
        Account account = new Account();
        account.setOwner(owner);
        account.setBalance(balance);
        return accountRepository.save(account);
    }

    public Account findAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found with ID: " + id));
    }

    @Transactional
    public void transferFunds(Long fromAccountId, Long toAccountId, double amount) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElse(null);
        Account toAccount = accountRepository.findById(toAccountId).orElse(null);
        if (fromAccount != null && toAccount != null) {
            if (fromAccount.getBalance() >= amount) {
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);
                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);
            } else {
                throw new RuntimeException("Insufficient funds in account ID: " + fromAccountId);
            }
        } else {
            throw new RuntimeException("Account not found with ID: " + (fromAccount == null ? fromAccountId : toAccountId));
        }
    }
}