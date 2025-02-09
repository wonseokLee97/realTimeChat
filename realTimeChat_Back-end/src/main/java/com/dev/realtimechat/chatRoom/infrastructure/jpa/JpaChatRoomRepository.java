package com.dev.realtimechat.chatRoom.infrastructure.jpa;

import com.dev.realtimechat.chatRoom.domain.ChatRoom;
import com.dev.realtimechat.chatRoom.infrastructure.ChatRoomRepository;
import com.dev.realtimechat.chatRoom.infrastructure.springdatajpa.SpringDataJpaChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaChatRoomRepository implements ChatRoomRepository {
    private final SpringDataJpaChatRoomRepository springDataJpaChatRoomRepository;

    @Override
    public void save(ChatRoom chatRoom) {
        springDataJpaChatRoomRepository.save(chatRoom);
    }

    @Override
    public Optional<ChatRoom> findByReceiverId(String receiverId) {
        return springDataJpaChatRoomRepository.findByReceiverId(receiverId);
    }

    @Override
    public Optional<ChatRoom> findById(String roomId) {
        return springDataJpaChatRoomRepository.findById(roomId);
    }
}


