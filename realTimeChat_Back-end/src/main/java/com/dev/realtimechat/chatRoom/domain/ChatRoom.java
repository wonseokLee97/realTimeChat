package com.dev.realtimechat.chatRoom.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class ChatRoom {

    @EqualsAndHashCode.Include
    @Id
    private String id;

    private String senderId;
    private String receiverId;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    private ChatRoom(String id, String senderId, String receiverId) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public static ChatRoom create(String senderId, String receiverId) {
        return ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
    }
}
