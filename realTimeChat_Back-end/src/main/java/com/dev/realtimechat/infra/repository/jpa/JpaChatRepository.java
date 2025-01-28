package com.dev.realtimechat.infra.repository.jpa;

import com.dev.realtimechat.domain.entity.Chat;
import com.dev.realtimechat.infra.repository.ChatRepository;
import com.dev.realtimechat.infra.repository.springdatajpa.SpringDataJpaChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaChatRepository implements ChatRepository {
    private final SpringDataJpaChatRepository springDataJpaChatRepository;

    @Override
    public void save(Chat chat) {
        springDataJpaChatRepository.save(chat);
    }

    @Override
    public List<Chat> findChatsWithPagination(String roomId, int limit, int offset) {
        return springDataJpaChatRepository.findChatsWithPagination(roomId, limit, offset);
    }
}
