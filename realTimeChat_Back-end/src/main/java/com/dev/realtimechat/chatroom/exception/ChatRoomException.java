package com.dev.realtimechat.chatroom.exception;

// Unchecked Exception
public class ChatRoomException extends RuntimeException{

    public ChatRoomException(String message) {
        super(message);
    }

    public ChatRoomException(String message, Throwable cause) {
        super(message, cause);
    }
}
