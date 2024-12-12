package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

    /**
     * Finds an account with the given username, and returns an optional account
     * @param username The account's username
     * @return An optional account that may or may not have an account that matches the account data
     */
    Optional<Account> findByUsername(String username);

    /**
     * Finds an account with the given username and password, and returns an optional account
     * @param username The account's username
     * @param password The account password
     * @return An optional account that may or may not have an account that matches the account data
     */
    Optional<Account> findByUsernameAndPassword(String username, String password);
}
