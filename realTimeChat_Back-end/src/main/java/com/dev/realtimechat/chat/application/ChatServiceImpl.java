package com.dev.realtimechat.chat.application;

import com.dev.realtimechat.chat.infrastructure.ChatRepository;
import com.dev.realtimechat.chatRoom.infrastructure.ChatRoomRepository;
import com.dev.realtimechat.shared.common.dto.ChatMessageDto;
import com.dev.realtimechat.chat.domain.Chat;
import com.dev.realtimechat.chatRoom.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Chat createChat(ChatMessageDto.ChatMessageRequest request, String randId, String nameTag, String ipAddress) {
        String message = request.message();
        String roomId = request.roomId();

        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(roomId);

        if (chatRoomOptional.isPresent()) {
            ChatRoom chatRoom = chatRoomOptional.get();
            Chat chat = Chat.create(chatRoom, randId, nameTag, message, ipAddress);
            chatRepository.save(chat);

            return chat;
        } else {
            log.error("채팅방이 존재하지 않습니다.");

            return null;
        }
    }

    @Override
    public List<ChatMessageDto.ChatMessageResponse> getChatList(String chatRoomId, int limit, int offset) {
        return chatRepository.findChatsWithPagination(chatRoomId, limit, offset).stream()
                .map(ChatMessageDto.ChatMessageResponse::create) // 각 Chat 객체를 ChatMessageResponse 로 변환
                .collect(Collectors.toList()); // 변환된 결과를 List 로 수집
    }
}
