package com.dev.realtimechat.application.dto;

import lombok.Getter;

public class ChatRoomDto {
    @Getter
    public static class CreatePrivateRoomRequest {
        private String senderId;
        private String receiverId;
    }

//    @Getter
    public record CreatePrivateRoomResponse(String senderId, String receiverId, String roomId) {}

//    @Getter
//    public static class CreatePrivateRoomResponse {
//        private final String senderId;
//        private final String receiverId;
//        private final String roomId;
//
//        public CreatePrivateRoomResponse(String senderId, String receiverId, String roomId) {
//            this.senderId = senderId;
//            this.receiverId = receiverId;
//            this.roomId = roomId;
//        }
//    }
}
