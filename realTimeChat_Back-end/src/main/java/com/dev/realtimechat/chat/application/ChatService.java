package com.dev.realtimechat.chat.application;

import com.dev.realtimechat.chat.domain.Chat;
import com.dev.realtimechat.shared.global.dto.ChatMessageDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ChatService {
    Chat createChat(ChatMessageDto.ChatMessageRequest request, String randId, String nameTag, String ipAddress);
    List<ChatMessageDto.ChatMessageResponse> getChatListByLimit(String chatRoomId, int limit);
    List<ChatMessageDto.ChatMessageResponse> getChatListByLastMessageId(String chatRoomId, int limit, int lastMessageId);
}
