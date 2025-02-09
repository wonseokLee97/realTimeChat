package com.dev.realtimechat.chatRoom.application;

import com.dev.realtimechat.shared.common.dto.ChatRoomDto;
import org.springframework.stereotype.Component;

@Component
public interface ChatRoomService {

    ChatRoomDto.CreatePrivateRoomResponse createPrivateRoom(ChatRoomDto.CreatePrivateRoomRequest request);
}
