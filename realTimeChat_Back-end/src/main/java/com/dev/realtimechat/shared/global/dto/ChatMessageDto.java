package com.dev.realtimechat.shared.global.dto;

import com.dev.realtimechat.chat.domain.Chat;
import lombok.*;

public class ChatMessageDto {
    public record ChatMessageRequest(String roomId, String message, String randId, String nameTag, String ipAddress) {}

    @Data
    public static class ChatMessageResponse {
        private final long id;
        private final String message;
        private final String randId;
        private final String nameTag;
        private final String createdAt;
        private final String ipAddress;

        // 정적 팩터리 메서드, 도메인 패턴
        @Builder
        private ChatMessageResponse(long id, String message, String randId, String nameTag, String createdAt, String ipAddress) {
            this.id = id;
            this.message = message;
            this.randId = randId;
            this.nameTag = nameTag;
            this.createdAt = createdAt;
            this.ipAddress = ipAddress;
        }

        public static ChatMessageResponse create(Chat chat) {
            return ChatMessageResponse.builder()
                    .id(chat.getId())
                    .message(chat.getMessage())
                    .randId(chat.getRandId())
                    .nameTag(chat.getNameTag())
                    .createdAt(String.valueOf(chat.getCreatedAt()))
                    .ipAddress(chat.getIpAddress())
                    .build();
        }
    }
}
