package com.dev.realtimechat.infra.repository;

import com.dev.realtimechat.domain.entity.Chat;

import java.util.List;

public interface ChatRepository {
    void save(Chat chat);
    List<Chat> findChatsWithPagination(String roomId, int limit, int offset);
}
