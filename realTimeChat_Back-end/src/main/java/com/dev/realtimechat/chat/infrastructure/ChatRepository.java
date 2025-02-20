package com.dev.realtimechat.chat.infrastructure;

import com.dev.realtimechat.chat.domain.Chat;

import java.util.List;

public interface ChatRepository {
    void save(Chat chat);
    List<Chat> findChatsWithPaginationByLimit(String roomId, int limit);
    List<Chat> findChatsWithPaginationByLastMessageId(String roomId, int lastMessageId, int offset);
}
