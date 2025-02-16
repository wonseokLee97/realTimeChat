package com.dev.realtimechat.chatroom.application;

import com.dev.realtimechat.shared.global.dto.ChatRoomDto;
import org.springframework.stereotype.Component;

@Component
public interface ChatRoomService {

    ChatRoomDto.CreatePrivateRoomResponse createPrivateRoom(ChatRoomDto.CreatePrivateRoomRequest request);
}
