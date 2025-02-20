package com.shadowveil.videoplatform.repository;

import com.shadowveil.videoplatform.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderId(Integer senderId);
    List<Message> findByReceiverId(Integer receiverId);
    List<Message> findBySenderIdAndReceiverId(Integer senderId, Integer receiverId);
    // For retrieving a conversation between two users, regardless of who sent which message
    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderBySentAtAsc(
            Integer sender1, Integer receiver1, Integer sender2, Integer receiver2);
}