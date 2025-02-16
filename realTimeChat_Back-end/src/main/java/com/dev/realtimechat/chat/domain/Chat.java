package com.dev.realtimechat.chat.domain;

import com.dev.realtimechat.chatroom.domain.ChatRoom;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class Chat {

    @Transient
    public static final String SEQUENCE_NAME = "chat_sequence";

    @Setter
    @Id
    private Long id;

    @Field("chat_room_id")
    private String chatRoomId;

    @Field("rand_id")
    private String randId;

    @Field("name_tag")
    private String nameTag;

    @Field("ip_address")
    private String ipAddress;

    @Field("message")
    private String message;

    @Field("created_at")
    private LocalDateTime createdAt;

    // 정적 팩터리 메서드, 도메인 패턴
    @Builder
    private Chat(ChatRoom chatRoom, String randId, String nameTag, String message, String ipAddress, LocalDateTime createdAt) {
        this.chatRoomId = chatRoom.getId();
        this.randId = randId;
        this.nameTag = nameTag;
        this.message = message;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public static Chat create(ChatRoom chatRoom, String randId, String nameTag, String message, String ipAddress) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .randId(randId)
                .nameTag(nameTag)
                .message(message)
                .ipAddress(ipAddress)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
