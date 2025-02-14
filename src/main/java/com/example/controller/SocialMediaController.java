package com.example.controller;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AuthenticationException;
import com.example.exception.MessageCreationException;
import com.example.exception.RegistrationException;
import com.example.exception.UpdateMessageException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }


    //User Story 1, register users
    @PostMapping(value = "/register")
    public ResponseEntity postRegister(@RequestBody Account account){
        Account addedAccount = accountService.persistAccount(account);
        return ResponseEntity.status(200).body(addedAccount);
        
    }

    //User Story 2, login/authenticate users
    @PostMapping(value = "/login")
    public ResponseEntity postLogin(@RequestBody Account account){
        Account foundAccount = accountService.login(account);
        return ResponseEntity.status(200).body(foundAccount);
    }

    //User Story 3, create messages
    @PostMapping(value = "/messages")
    public ResponseEntity postMessage(@RequestBody Message message){
        Message addedMessage = messageService.persistMessage(message);
        return ResponseEntity.status(200).body(addedMessage);
    }

    //User Story 4, get all messages
    @GetMapping("/messages")
    public ResponseEntity getAllMessages(){
        return ResponseEntity.status(200).body(messageService.getAllMessages());
        
    }

    //User Story 5, get message by message id
    @GetMapping("/messages/{messageId}")
    public ResponseEntity getMessageById(@PathVariable int messageId){
        return ResponseEntity.status(200).body(messageService.getMessageById(messageId));
        
    }

     //User Story 6, delete message given a message id
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity deleteMessageById(@PathVariable int messageId){
        int deletedRows = messageService.deleteMessageById(messageId);
        //If no rows were deleted, then we just return an empty body. Not really an exception issue
        if(deletedRows == 0){
            return ResponseEntity.status(200).build();
        }
        //If there were rows deleted, then the body contains the number(which is only 1 as of now)
        return ResponseEntity.status(200).body(deletedRows);
         
    }
    
    //User Story 7, update messages by account id using updated message text
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity patchMessageByMessageId(@PathVariable int messageId, @RequestBody Message newMessage){
        //This returns the number of rows affected in the body. There are exceptions for the messageText constraints
        //and determining if the message exists already.
        return ResponseEntity.status(200).body(messageService.updateMessageTextByMessageId(messageId, newMessage));
        
    }

    //User Story 8, get messages by account id
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getMessagesByAccountId(@PathVariable int accountId){
        return ResponseEntity.status(200).body(messageService.getMessagesByPostedBy(accountId));
        
    }

    //This will handle the exception for when creating messages and the message text doesn't follow
    //the constraints for that data or the user doesn't exist yet
    @ExceptionHandler(MessageCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String messageCreationExceptionHandler(MessageCreationException ex) {
        return ex.getMessage();
    }

    //This will handle the exception for when registering and the username or password doesn't follow
    //the constraints for that data
    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String registrationExceptionHandler(RegistrationException ex) {
        return ex.getMessage();
    }

    //This will handle the exception that the user was unable to authenticate and login, aka, wrong
    //username and password was given to the repository. 
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody String authenticationExceptionHandler(AuthenticationException ex) {
        return ex.getMessage();
    }

    //This will handle the exception for when updating messages texts and the message text doesn't follow
    //the constraints for that data or the message didn't exist yet before the update
    @ExceptionHandler(UpdateMessageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String updateMessageExceptionHandler(UpdateMessageException ex){
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
