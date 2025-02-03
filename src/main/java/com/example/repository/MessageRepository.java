package com.example.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;
import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    Message getById(Integer messageId);
    Optional<Message> findById(Integer messageId);
    List<Message> findAll();
    void deleteById(Integer messageId);
    boolean existsById(Integer messageId);
    List<Message> findAllByPostedBy(Integer postedBy);
}
