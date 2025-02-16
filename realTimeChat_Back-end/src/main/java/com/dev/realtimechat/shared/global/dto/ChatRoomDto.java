package com.dev.realtimechat.shared.common.dto;

public class ChatRoomDto {
    public record CreatePrivateRoomRequest(String senderId, String receiverId) {}
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
