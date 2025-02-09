package com.dev.realtimechat.chat.presentation;

import com.dev.realtimechat.chat.application.ChatService;
import com.dev.realtimechat.shared.common.api.ApiResponse;
import com.dev.realtimechat.shared.common.type.SuccessType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public ApiResponse<?> getChatList(
            @RequestParam("roomId") String roomId,
            @RequestParam("limit") int limit,
            @RequestParam("offset") int offset
    ) {

        log.info("Get chat list");

        return ApiResponse.success(
                chatService.getChatList(roomId, limit, offset),
                SuccessType.SUCCESS_GET_CHAT_LIST);
    }
}
