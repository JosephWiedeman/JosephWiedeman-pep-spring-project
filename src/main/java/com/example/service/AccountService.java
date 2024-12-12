package com.example.service;

import java.util.Optional;

import javax.security.auth.message.AuthException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.repository.AccountRepository;
import com.example.entity.Account;
import com.example.exception.AuthenticationException;
import com.example.exception.RegistrationException;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    //This does dependency interjection using autowired, giving us the account repository
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    /**
     * This will persist the account, aka, add it to the repository database
     * @param account The account to be added into the database
     * @return The account that was added with the updated account_id, since SQL does that for us
     */
    public Account persistAccount(Account account){
        //The best practices I found is that the service layer should throw the exceptions, not the controller
        if(account.getUsername().equals("") || account.getPassword().length() < 4){
            throw new RegistrationException("The username or password doesn't fit the requirements");
        }
        //This will throw a data integrity exception for us if the data doesn't fit the database constraints.
        //Such as, having two of the same values for a unique username varchar. 
        return accountRepository.save(account);
    }

    /**
     * This will help with logining by determining that the username and password are in the repository
     * @param account The account to determine if it exists. It doesn't have an account_id, so have to
     *                determine if the account exists using the username and password
     * @return The account that matches the username and password, or throws an authentication exception
     */
    public Account login(Account account){
        Optional<Account> optionalAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if(optionalAccount.isPresent()){
            return optionalAccount.get();
        }
        throw new AuthenticationException("The username or password entered was not found");
    }
}
