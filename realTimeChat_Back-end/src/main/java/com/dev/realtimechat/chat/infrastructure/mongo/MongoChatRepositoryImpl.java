package com.dev.realtimechat.chat.infrastructure.mongo;

import com.dev.realtimechat.chat.domain.Chat;
import com.dev.realtimechat.chat.infrastructure.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j
public class MongoChatRepositoryImpl implements ChatRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void save(Chat chat) {
        mongoTemplate.save(chat);
    }

    @Override
    public List<Chat> findChatsWithPaginationByLimit(String roomId, int limit) {
        Query query = new Query(Criteria.where("chat_room_id").is(roomId))
                .with(Sort.by(Sort.Direction.DESC, "created_at"))  // created_at 기준 내림차순 정렬 추가
                .limit(limit);
        return mongoTemplate.find(query, Chat.class);
    }

    @Override
    public List<Chat> findChatsWithPaginationByLastMessageId(String roomId, int limit, int lastMessageId) {
        Query query = new Query(Criteria.where("chat_room_id").is(roomId)
                .and("_id").lt(lastMessageId))
                .with(Sort.by(Sort.Direction.DESC, "created_at"))  // created_at 기준 내림차순 정렬 추가
                .limit(limit);

        log.info(lastMessageId +" ");

        List<Chat> chats = mongoTemplate.find(query, Chat.class);
        chats.forEach(chat -> log.info("Chat ID: {}, CreatedAt: {}", chat.getId(), chat.getCreatedAt()));


        return chats;
    }
}