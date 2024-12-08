package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.repository.AccountRepository;
import com.example.entity.Account;
import com.example.exception.RegistrationException;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account persistAccount(Account account){
        //The best practices I found is that the service layer should throw the exceptions, not the controller
        if(account.getUsername().equals("") || account.getPassword().length() < 4){
            throw new RegistrationException("The username or password doesn't fit the requirements");
        }
        return accountRepository.save(account);
    }
}
