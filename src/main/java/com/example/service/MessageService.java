package com.example.service;

import java.util.Optional;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.MessageCreationException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message persistMessage(Message message){
        
        //This throws an exception if the message doesn't meet the constraints or the user who posted it doesn't exist.
        //They also each have their own exception message that should be printed.
        if(message.getMessageText().equals("") 
                || message.getMessageText().length() > 255){
            throw new MessageCreationException("The message should not be blank or exceed 255 characters.");
        }if(!accountRepository.existsById(message.getPostedBy())){
            throw new MessageCreationException("The user account used has not been registered.");
        }
        
        return messageRepository.save(message);
    }

    public Message getMessageById(int messageId){
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        }
        return null;
    }

    //Gets all the messages by the account posters id
    public List<Message> getMessagesByPostedBy(int postedBy){
        return messageRepository.findMessagesByPostedBy(postedBy);
    }
}
