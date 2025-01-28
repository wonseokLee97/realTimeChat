package com.dev.realtimechat.application;

import com.dev.realtimechat.application.dto.ChatRoomDto;
import org.springframework.stereotype.Component;

@Component
public interface ChatRoomService {

    ChatRoomDto.CreatePrivateRoomResponse createPrivateRoom(ChatRoomDto.CreatePrivateRoomRequest request);
}
