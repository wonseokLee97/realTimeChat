package com.dev.realtimechat.chat.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "counter")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Sequence {
    @Id
    private String id;
    private int seq;
}
