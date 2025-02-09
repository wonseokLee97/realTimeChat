package com.dev.realtimechat.chat.domain;

import com.dev.realtimechat.chatRoom.domain.ChatRoom;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    private String randId;

    private String nameTag;

    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreatedDate
    private LocalDateTime createdAt;

    // 정적 팩터리 메서드, 도메인 패턴
    @Builder
    private Chat(ChatRoom chatRoom, String randId, String nameTag, String message, String ipAddress) {
        this.chatRoom = chatRoom;
        this.randId = randId;
        this.nameTag = nameTag;
        this.message = message;
        this.ipAddress = ipAddress;
    }

    public static Chat create(ChatRoom chatRoom, String randId, String nameTag, String message, String ipAddress) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .randId(randId)
                .nameTag(nameTag)
                .message(message)
                .ipAddress(ipAddress)
                .build();
    }
}
