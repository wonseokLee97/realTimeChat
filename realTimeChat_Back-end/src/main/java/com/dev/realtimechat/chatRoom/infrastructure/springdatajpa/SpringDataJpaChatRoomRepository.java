package com.dev.realtimechat.chatRoom.infrastructure.springdatajpa;

import com.dev.realtimechat.chatRoom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SpringDataJpaChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByReceiverId(String receiverId);
    Optional<ChatRoom> findById(String id);
}
