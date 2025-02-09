package com.dev.realtimechat.shared.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JwtProvider jwtProvider;
    public @Value("${server.chat-room-id}") String roomId;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 요청의 경우
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 헤더에서 JWT 토큰 추출
            String ipAddress = accessor.getFirstNativeHeader("X-Client-IP");
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // "Bearer "를 제거하여 실제 토큰만 추출

                try {
                    jwtProvider.validateToken(token, ipAddress);
                } catch (ExpiredJwtException e) {
                    throw e;
                } catch (Exception e) {
                    throw new BadCredentialsException("Invalid JWT token", e);
                }

            } else {
                log.warn("Authorization header is missing or invalid");
                throw new IllegalArgumentException("Authorization header is missing or invalid");
            }
        }

        return message;
    }
}
