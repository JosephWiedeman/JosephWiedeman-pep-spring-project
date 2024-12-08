package com.example.controller;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.exception.RegistrationException;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;

    @Autowired
    public SocialMediaController(AccountService accountService){
        this.accountService = accountService;
    }


    @PostMapping(value = "/register")
    public ResponseEntity postRegister(@RequestBody Account account){
        Account addedAccount = accountService.persistAccount(account);
        return ResponseEntity.status(200).body(addedAccount);
        
    }

    //This will handle the exception for when registering and the username or password doesn't follow
    //the constraints for that data
    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String registrationErrorHandler(RegistrationException ex) {
        return ex.getMessage();
    }

    //This is an exception handler for when the registration already has a unique username, and it violates
    //the integreity of the datatable
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody String dataIntegrityViolationHandler(DataIntegrityViolationException ex) {
        return ex.getMessage();
    }
}
