package com.dev.realtimechat.application.api;

import com.dev.realtimechat.application.type.ErrorType;
import com.dev.realtimechat.application.type.Type;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

/**
 * 실패(에러발생)시 응답하는 클래스
 */
@Getter
@Builder
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
