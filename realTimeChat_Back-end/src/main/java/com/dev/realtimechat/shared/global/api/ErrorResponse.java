package com.dev.realtimechat.shared.common.api;

import lombok.Builder;
import lombok.Getter;

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
