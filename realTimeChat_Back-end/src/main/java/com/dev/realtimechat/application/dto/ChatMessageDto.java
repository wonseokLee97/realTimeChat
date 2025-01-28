package com.dev.realtimechat.application.dto;

import com.dev.realtimechat.domain.entity.Chat;
import lombok.*;

public class ChatMessageDto {
    @Data
    @Builder
    public static class ChatMessageRequest {
        private String roomId;
        private String message;
        private String randId;
        private String nameTag;
        private String ipAddress;
    }

    @Data
    @Builder
    public static class ChatMessageResponse {
        private String message;
        private String randId;
        private String nameTag;
        private String createdAt;
        private String ipAddress;


        public static ChatMessageResponse create(Chat chat) {
            return ChatMessageResponse.builder()
                    .message(chat.getMessage())
                    .randId(chat.getRandId())
                    .nameTag(chat.getNameTag())
                    .createdAt(String.valueOf(chat.getCreatedAt()))
                    .ipAddress(chat.getIpAddress())
                    .build();
        }
    }
}
