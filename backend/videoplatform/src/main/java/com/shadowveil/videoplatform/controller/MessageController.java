package com.shadowveil.videoplatform.controller;

import com.shadowveil.videoplatform.dto.MessageDto;
import com.shadowveil.videoplatform.entity.Message;
import com.shadowveil.videoplatform.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<MessageDto.Response>> getAllMessages() {
        List<MessageDto.Response> messages = messageService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDto.Response> getMessageById(@PathVariable Integer id) {
        Optional<MessageDto.Response> message = messageService.getMessageById(id);
        return message.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<MessageDto.Response>> getMessagesBySenderId(@PathVariable Integer senderId) {
        List<MessageDto.Response> messages = messageService.getMessagesBySenderId(senderId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<MessageDto.Response>> getMessagesByReceiverId(@PathVariable Integer receiverId) {
        List<MessageDto.Response> messages = messageService.getMessagesByReceiverId(receiverId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/sender/{senderId}/receiver/{receiverId}")
    public ResponseEntity<List<MessageDto.Response>> getMessagesBySenderAndReceiver(
            @PathVariable Integer senderId,
            @PathVariable Integer receiverId) {
        List<MessageDto.Response> messages = messageService.getMessagesBySenderAndReceiver(senderId, receiverId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/conversation/{user1Id}/{user2Id}")
    public ResponseEntity<List<MessageDto.Response>> getConversation(
            @PathVariable Integer user1Id,
            @PathVariable Integer user2Id) {
        List<MessageDto.Response> conversation = messageService.getConversation(user1Id, user2Id);
        return new ResponseEntity<>(conversation, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MessageDto.Response> createMessage(@Valid @RequestBody MessageDto.Request messageDto) {
        Message createdMessage = messageService.createMessage(messageDto);
        MessageDto.Response responseDto = messageService.convertToDto(createdMessage); // Convert to response DTO
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageDto.Response> updateMessage(@PathVariable Integer id, @Valid @RequestBody MessageDto.Request messageDto) {
        Message updatedMessage = messageService.updateMessage(id, messageDto);
        MessageDto.Response responseDto = messageService.convertToDto(updatedMessage);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer id) {
        messageService.deleteMessage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}