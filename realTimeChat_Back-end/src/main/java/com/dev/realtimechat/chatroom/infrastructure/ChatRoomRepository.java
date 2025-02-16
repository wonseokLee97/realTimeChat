package com.dev.realtimechat.chatroom.infrastructure;

import com.dev.realtimechat.chatroom.domain.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository {
    void save(ChatRoom chatRoom);
    Optional<ChatRoom> findByReceiverId(String receiverId);
    Optional<ChatRoom> findById(String roomId);
}
