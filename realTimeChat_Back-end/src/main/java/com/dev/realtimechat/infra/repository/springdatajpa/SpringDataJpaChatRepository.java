package com.dev.realtimechat.infra.repository.springdatajpa;

import com.dev.realtimechat.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataJpaChatRepository extends JpaRepository<Chat, Long> {

    @Query(value = "SELECT * " +
            "FROM chat " +
            "WHERE chatroom_id = :roomId " +
            "ORDER BY created_at DESC " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Chat> findChatsWithPagination(
            @Param("roomId") String roomId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
    // offset: 어디서 부터
    // limit: 몇 개 까지
}


