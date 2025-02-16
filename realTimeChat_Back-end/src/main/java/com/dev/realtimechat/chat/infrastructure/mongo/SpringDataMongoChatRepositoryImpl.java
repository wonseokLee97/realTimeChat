package com.dev.realtimechat.chat.infrastructure.mongo;

import com.dev.realtimechat.chat.domain.Chat;
import com.dev.realtimechat.chat.infrastructure.ChatRepository;
import com.dev.realtimechat.chat.infrastructure.springdata.springdatamongo.SpringDataMongoChatRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class SpringDataMongoChatRepositoryImpl implements ChatRepository {
    private final SpringDataMongoChatRepository springDataMongoChatRepository;

    @Override
    public void save(Chat chat) {
        springDataMongoChatRepository.save(chat);
    }

    @Override
    public List<Chat> findChatsWithPaginationByOffset(String roomId, int limit, int offset) {
        return springDataMongoChatRepository.findChatsWithPaginationByOffset(roomId, limit, offset);
    }

    @Override
    public List<Chat> findChatsWithPaginationByLastMessageId(String roomId, int lastMessageId, int offset) {
        return springDataMongoChatRepository.findChatsWithPaginationByLastMessageId(roomId, lastMessageId, offset);
    }
}
