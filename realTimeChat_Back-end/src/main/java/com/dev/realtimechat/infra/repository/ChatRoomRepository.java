package com.dev.realtimechat.infra.repository;

import com.dev.realtimechat.domain.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository {
    void save(ChatRoom chatRoom);
    Optional<ChatRoom> findByReceiverId(String receiverId);
    Optional<ChatRoom> findById(String roomId);
}
