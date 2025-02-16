package com.dev.realtimechat.chat.handler;

import com.dev.realtimechat.chat.exception.CustomSocketException;
import com.dev.realtimechat.shared.global.api.ApiResponse;
import com.dev.realtimechat.shared.global.type.http.HttpErrorType;
import com.dev.realtimechat.shared.global.type.ws.WsErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.SocketException;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class WebSocketExceptionHandler {

    @MessageExceptionHandler(CustomSocketException.NoSuchChatRoomExceptionCustom.class)
    @SendToUser(destinations = "/queue/errors", broadcast = false)  // 해당 Session 의 User 에게만 예외 메시지 전송
    protected ApiResponse<?> handleNoChatRoomException(CustomSocketException.NoSuchChatRoomExceptionCustom e) {
        return ApiResponse
                .error(
                        e.getMessage(),
                        HttpErrorType.NOT_FOUND_CHATROOM
                );
    }

    @MessageExceptionHandler(SocketException.class)
    @SendToUser(destinations = "/queue/errors", broadcast = false)  // 해당 Session 의 User 에게만 예외 메시지 전송
    protected ApiResponse<?> handleSocketException(SocketException e) {
        return ApiResponse
                .error(
                        e.getMessage(),
                        WsErrorType.SOCKET_ERROR,
                        e.getCause()
                );
    }
}
