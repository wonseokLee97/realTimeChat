package com.dev.realtimechat.application;

import com.dev.realtimechat.application.dto.ChatMessageDto;
import com.dev.realtimechat.domain.entity.Chat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ChatService {
    Chat createChat(ChatMessageDto.ChatMessageRequest request, String randId, String nameTag, String ipAddress);
    List<ChatMessageDto.ChatMessageResponse> getChatList(String chatRoomId, int limit, int offset );
}
