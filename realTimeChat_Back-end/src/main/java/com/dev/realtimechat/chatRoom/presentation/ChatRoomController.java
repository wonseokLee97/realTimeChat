package com.dev.realtimechat.chatRoom.presentation;

import com.dev.realtimechat.chatRoom.application.ChatRoomService;
import com.dev.realtimechat.shared.common.api.ApiResponse;
import com.dev.realtimechat.shared.common.dto.ChatRoomDto;
import com.dev.realtimechat.shared.common.type.SuccessType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    // 채팅방 생성 - MVP
    // 각 사용자 두 명만 사용 가능한 채팅방이다. 각 사용자는 채팅방을 sub 하고, pub 메시지를 전달받는다.
    // 1. 메시지를 전달하기 전 까지는 채팅방이 생성되지 않는다.
    // 2. 메시지를 전달하게 되면 두 사용자는 채팅방 Topic 을 Sub 한다.
    @PostMapping("/private")
    public ApiResponse<?> createPrivateRoom(
            @RequestBody ChatRoomDto.CreatePrivateRoomRequest request) {

        return ApiResponse.success(
                chatRoomService.createPrivateRoom(request),
                SuccessType.SUCCESS_ROOM_CREATE);
    }
}
