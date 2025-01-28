package com.dev.realtimechat.infra.repository.springdatajpa;

import com.dev.realtimechat.application.dto.ChatMessageDto;
import com.dev.realtimechat.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SpringDataJpaChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByReceiverId(String receiverId);
    Optional<ChatRoom> findById(String id);
}
