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

    List<Message> findMessagesByPostedBy(int postedBy);
}
