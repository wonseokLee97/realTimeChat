package com.dev.realtimechat.infra.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.messaging.*;

@Component
@Slf4j
public class StompListener {
    private final SubProtocolWebSocketHandler subProtocolWebSocketHandler;
    private final SimpMessagingTemplate messagingTemplate;

    public StompListener(WebSocketHandler webSocketHandler, SimpMessagingTemplate messagingTemplate) {
        this.subProtocolWebSocketHandler = (SubProtocolWebSocketHandler) webSocketHandler;
        this.messagingTemplate = messagingTemplate;
    }

    // CONNECT -> 구독 발생 이후..
    @EventListener
    public void handleSessionSubscribed(SessionSubscribeEvent event) {
        int webSocketSessions = subProtocolWebSocketHandler.getStats().getWebSocketSessions();
        log.info("CONNECT -> 구독 발생 이후..");
        messagingTemplate.convertAndSend("/sub/channel/entry", webSocketSessions);
    }

    // 세션 종료 이후..
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        int webSocketSessions = subProtocolWebSocketHandler.getStats().getWebSocketSessions();
        messagingTemplate.convertAndSend("/sub/channel/entry", webSocketSessions);
    }
}
