package com.example.service;

import java.util.Optional;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.MessageCreationException;
import com.example.exception.UpdateMessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    //This does dependency interjection using autowired, giving us the message and account repository
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * This will persist messages, or create and add them to the database and repository
     * @param message The message to be created and added
     * @return The message with an updated message_id created by SQL, 
     *         or throws an exception if it doesn't follow the constraints
     */
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

    /**
     * This gets all messages and returns as a list
     * @return The list of all messages posted, or an empty list if no messages exist
     */
    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    /**
     * This will get a message based on a message_id
     * @param messageId The message_id for certain message to get
     * @return The message with the given messageId, or null if message does not exist
     */
    public Message getMessageById(int messageId){
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        }
        return null;
    }

    /**
     * Gets all the messages by the account posters id
     * @param postedBy The posted_by account, aka the account_id of the account that posted the messages
     * @return The messages posted by the account, or an empty list if no messages exist
     */
    public List<Message> getMessagesByPostedBy(int postedBy){
        return messageRepository.findMessagesByPostedBy(postedBy);
    }

    /**
     * Deletes a message with a given message_id in the repository. If no message with that ID, then no deletion necessary,
     * just return no rows were deleted 
     * @param messageId The message_id of the message to be deleted
     * @return The number of rows affected by deletion, which either is 1 if the message exists, or 0 if it doesn't
     */
    public int deleteMessageById(int messageId){
        if(messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            //Only one row is affected by this deleteion
            return 1;
        }return 0; //No message to be deleted, no rows affected
    }

    /**
     * Updates a certain message, with a given message_id, using gthe given updated message text
     * @param messageId The message_id of the message to be updated
     * @param newMessage The new message with the updated message text, to be used to update the message in the database
     * @return The updated message with the updated messageText, or throws an exception if the message text doesn't
     *         follow the constraints or the message doesn't exist, and therefore can't be updated
     */
    public int updateMessageTextByMessageId(int messageId, Message newMessage){
        //If the messageText is black or is greater than 255 characters, then the exception is thrown
        if(newMessage.getMessageText().equals("") || newMessage.getMessageText().length() > 255){
            throw new UpdateMessageException("The updated message should not be blank or exceed 255 characters.");
        }
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        //If the message does exist and the updated message fits the constraints, update messages text 
        //and save the new updated message. Return that 1 row was affected.
        if(optionalMessage.isPresent()){
            Message updatedMessage = optionalMessage.get();
            updatedMessage.setMessageText(newMessage.getMessageText());
            messageRepository.save(updatedMessage);
            return 1;
        }
        //If the message didn;t already exist, the exception is thrown
        throw new UpdateMessageException("The message trying to be updated could not be found.");
        
    }
}
