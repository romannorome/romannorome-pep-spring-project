package com.example.controller;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import javassist.NotFoundException;

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

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Map<String, String> register){
        String username = register.get("username");
        String password = register.get("password");

        try{
            Account newAccount = accountService.register(username, password);
            return ResponseEntity.status(HttpStatus.OK).body(newAccount);
        } catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Map<String, String> login){
        String username = login.get("username");
        String password = login.get("password");

        try{
            Account account = accountService.login(username, password);
            return ResponseEntity.status(HttpStatus.OK).body(account);
        } catch(NullPointerException | IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        String messageText = message.getMessageText();
        Integer postedBy = message.getPostedBy();
        Long timePostedEpoch = message.getTimePostedEpoch();

        try{
            Message newMessage = messageService.createMessage(messageText, postedBy, timePostedEpoch);
            return ResponseEntity.status(HttpStatus.OK).body(newMessage);
        } catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(messageService.getAllMessages());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessageById(messageId));
        }catch(EntityNotFoundException e){
            return null;
        }
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId){
        int rowsDeleted = messageService.deleteMessage(messageId);

        if(rowsDeleted == 1){
            return ResponseEntity.ok(1);
        }
        else{
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Map<String, String> body){

        String newMessage = body.get("messageText");
        
        boolean rowsUpdated  = messageService.updateMessage(messageId, newMessage);

        if(rowsUpdated){
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }

        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessageByPostedBy(@PathVariable Integer accountId){
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessagesByPostedBy(accountId));
    }
}
