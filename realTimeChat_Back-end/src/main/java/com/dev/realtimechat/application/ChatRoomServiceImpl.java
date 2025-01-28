package com.dev.realtimechat.application;

import com.dev.realtimechat.application.dto.ChatRoomDto;
import com.dev.realtimechat.domain.entity.ChatRoom;
import com.dev.realtimechat.infra.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    // 생성자 주입 생략
    @Override
    public ChatRoomDto.CreatePrivateRoomResponse createPrivateRoom(
            ChatRoomDto.CreatePrivateRoomRequest request) {

        // 1:1 채팅 대상자의 Id
        String senderId = request.getSenderId();
        String receiverId = request.getReceiverId();

        Optional<ChatRoom> findChatRoom = chatRoomRepository.findByReceiverId(receiverId);

        if (findChatRoom.isEmpty()) {
            ChatRoom chatRoom = ChatRoom.create(senderId, receiverId);
            chatRoomRepository.save(chatRoom);
            return new ChatRoomDto.CreatePrivateRoomResponse(senderId, receiverId, String.valueOf(chatRoom.getId()));
        } else {
            log.error("예외: 이미 채팅방이 존재한다. - 기존의 채팅방으로 redirection");
            return null;
        }
    }
}
