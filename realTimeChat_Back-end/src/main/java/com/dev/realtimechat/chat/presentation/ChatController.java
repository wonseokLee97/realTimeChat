package com.dev.realtimechat.chat.presentation;

import com.dev.realtimechat.chat.application.ChatService;
import com.dev.realtimechat.shared.global.api.ApiResponse;
import com.dev.realtimechat.shared.global.type.http.HttpSuccessType;
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

    // 최초 로딩 (OFFSET 기반 페이징)
    @GetMapping("/offset")
    public ApiResponse<?> getChatListByOffset(
            @RequestParam("roomId") String roomId,
            @RequestParam("limit") int limit,
            @RequestParam("offset") int offset
    ) {
        log.info("Get chat list By Offset");
        return ApiResponse.success(
                chatService.getChatListByOffset(roomId, limit, offset),
                HttpSuccessType.SUCCESS_GET_CHAT_LIST);
    }

    // 추가 로딩 (lastMessageId 기반 페이징)
    @GetMapping("/lastMessageId")
    public ApiResponse<?> getChatListByLastMessageId(
            @RequestParam("roomId") String roomId,
            @RequestParam("limit") int limit,
            @RequestParam("lastMessageId") int lastMessageId
    ) {
        log.info("Get chat list By LastMessageId");
        return ApiResponse.success(
                chatService.getChatListByLastMessageId(roomId, limit, lastMessageId),
                HttpSuccessType.SUCCESS_GET_CHAT_LIST);
    }
}
