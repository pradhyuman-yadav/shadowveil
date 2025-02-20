package com.shadowveil.videoplatform.service;

import com.shadowveil.videoplatform.dto.MessageDto;
import com.shadowveil.videoplatform.dto.UserDto;
import com.shadowveil.videoplatform.entity.Message;
import com.shadowveil.videoplatform.entity.User;
import com.shadowveil.videoplatform.exception.ResourceNotFoundException;
import com.shadowveil.videoplatform.repository.MessageRepository;
import com.shadowveil.videoplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserService userService; // Inject for DTO conversion

    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.userService = userService; // Initialize
    }

    @Transactional(readOnly = true)
    public List<MessageDto.Response> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MessageDto.Response> getMessageById(Integer id) {
        return messageRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<MessageDto.Response> getMessagesBySenderId(Integer senderId) {
        return messageRepository.findBySenderId(senderId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MessageDto.Response> getMessagesByReceiverId(Integer receiverId) {
        return messageRepository.findByReceiverId(receiverId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MessageDto.Response> getMessagesBySenderAndReceiver(Integer senderId, Integer receiverId) {
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MessageDto.Response> getConversation(Integer user1Id, Integer user2Id) {
        return messageRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderBySentAtAsc(
                        user1Id, user2Id, user2Id, user1Id).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Message createMessage(MessageDto.Request messageDto) {
        User sender = userRepository.findById(messageDto.senderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender with ID " + messageDto.senderId() + " not found."));
        User receiver = userRepository.findById(messageDto.receiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver with ID " + messageDto.receiverId() + " not found."));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(messageDto.content());
        message.setIsRead(false); // Set isRead to false initially
        return messageRepository.save(message);
    }

    @Transactional
    public Message updateMessage(Integer id, MessageDto.Request messageDto) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message with ID " + id + " not found"));

        // Validate sender/receiver if they are being changed.
        if (messageDto.senderId() != null && !message.getSender().getId().equals(messageDto.senderId())) {
            User newSender = userRepository.findById(messageDto.senderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sender with ID " + messageDto.senderId() + " not found."));
            message.setSender(newSender);
        }
        if (messageDto.receiverId() != null && !message.getReceiver().getId().equals(messageDto.receiverId())) {
            User newReceiver = userRepository.findById(messageDto.receiverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Receiver with ID " + messageDto.receiverId() + " not found."));
            message.setReceiver(newReceiver);
        }

        if(messageDto.content() != null) message.setContent(messageDto.content());
        return messageRepository.save(message);
    }

    @Transactional
    public void deleteMessage(Integer id) {
        if (!messageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Message with ID " + id + " not found");
        }
        messageRepository.deleteById(id);
    }

    public MessageDto.Response convertToDto(Message message) {
        UserDto.Response senderDto = null;
        if(message.getSender() != null){
            senderDto =  userService.convertToDto(message.getSender()); // Use UserService
        }

        UserDto.Response receiverDto = null;
        if(message.getReceiver() != null){
            receiverDto = userService.convertToDto(message.getReceiver()); // Use UserService
        }

        return new MessageDto.Response(
                message.getId(),
                senderDto,
                receiverDto,
                message.getContent(),
                message.getSentAt(),
                message.getIsRead()
        );
    }
}