package com.dev.realtimechat.shared.common.dto;

import com.dev.realtimechat.chat.domain.Chat;
import lombok.*;

public class ChatMessageDto {
    public record ChatMessageRequest(String roomId, String message, String randId, String nameTag, String ipAddress) {}

    @Data
    public static class ChatMessageResponse {
        private final String message;
        private final String randId;
        private final String nameTag;
        private final String createdAt;
        private final String ipAddress;

        // 정적 팩터리 메서드, 도메인 패턴
        @Builder
        private ChatMessageResponse(String message, String randId, String nameTag, String createdAt, String ipAddress) {
            this.message = message;
            this.randId = randId;
            this.nameTag = nameTag;
            this.createdAt = createdAt;
            this.ipAddress = ipAddress;
        }

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
