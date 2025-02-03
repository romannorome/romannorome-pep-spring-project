package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    public AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account register(String username, String password){

        if(username == null || username.trim().isEmpty()){
            throw new IllegalArgumentException("Username Must Not Be Empty");
        }

        if(password == null || password.trim().length() < 4){
            throw new IllegalArgumentException("Password Must Be longer than 4 characters");
        }

        if(accountRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("Username Already In Use");
        }
        
        Account newAccount = new Account();
        newAccount.setUsername(username);
        newAccount.setPassword(password);
        return accountRepository.save(newAccount);
    }

    public Account login(String username, String password){
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Account Not Found"));

        if(!account.getPassword().equals(password)){
            throw new IllegalArgumentException("Incorrect Username or Password");
        }
        return account;
    }
}
