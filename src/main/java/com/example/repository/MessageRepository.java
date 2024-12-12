package com.example.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.example.entity.Message;
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    
    /**
     * Gets all the messages for a certain acount that posted them and returns a list of messages
     * @param postedBy The account_id of an account that posted messages
     * @return A list of messages that the account posted, or empty if no messages posted
     */
    List<Message> findMessagesByPostedBy(int postedBy);
}
