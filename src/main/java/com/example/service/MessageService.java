package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class MessageService {

    public MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message createMessage(String messageTxt, Integer postedBy, Long timePostedEpoch){
        if(messageTxt.length() > 255){
            throw new IllegalArgumentException("Message Must Be Less Than 255 Characters");
        }
        if(messageTxt.trim().isEmpty()){
            throw new IllegalArgumentException("Message Cannot Be Empty");
        }

        Message newMessage = new Message();
        newMessage.setMessageText(messageTxt);
        newMessage.setPostedBy(postedBy);
        newMessage.setTimePostedEpoch(timePostedEpoch);
        return messageRepository.save(newMessage);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId){
        return messageRepository.findById(messageId).orElseThrow(() -> new EntityNotFoundException("Message not found with ID: " + messageId));
    }

    public int deleteMessage(Integer messageId){
        if(messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            return 1;    
        }
        return 0;
    }

    public boolean updateMessage(Integer messageId, String newMessage){
        Optional<Message> optMessage = messageRepository.findById(messageId);
        if(optMessage.isPresent()){
            if (newMessage != null && newMessage.length() <= 255 && !newMessage.trim().isEmpty()) {
                Message existingMessage = optMessage.get();

                existingMessage.setMessageText(newMessage);
                messageRepository.save(existingMessage);

                return true;
            }
            return false;
        }
        return false;
    }

    public List<Message> getMessagesByPostedBy(Integer postedBy){
        return messageRepository.findAllByPostedBy(postedBy);
    }
}
