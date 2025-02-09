package com.dev.realtimechat.chatRoom.infrastructure;

import com.dev.realtimechat.chatRoom.domain.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository {
    void save(ChatRoom chatRoom);
    Optional<ChatRoom> findByReceiverId(String receiverId);
    Optional<ChatRoom> findById(String roomId);
}
