package com.dev.realtimechat.application.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorType implements Type{

    // 400 BAD_REQUEST: 잘못된 요청
    INVALID_PARAMETER(
            HttpStatus.BAD_REQUEST, "파라미터 값을 확인해주세요."),
    ALREADY_CREATED_TOKEN(
            HttpStatus.BAD_REQUEST, "이미 토큰이 존재합니다"),


    // 401 UNAUTHORIZED: 유효하지 않음
    INVALID_TOKEN(
            HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),


    // 403 FORBIDDEN: 권한 없음
    FORBIDDEN_ACCESS(
            HttpStatus.FORBIDDEN, "권한이 없는 접근입니다."),


    // 404 NOT_FOUND: 잘못된 리소스 접근
    NOT_FOUND_USER(
            HttpStatus.NOT_FOUND, "존재하지 않는 유저 입니다."),
    NOT_FOUND_TOKEN(
            HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다"),

    // 409 CONFLICT: 요청이 서버의 현재 상태와 충돌
    ALREADY_CREATED_USER_ID(
            HttpStatus.CONFLICT, "중복된 유저 아이디 입니다."),
    ALREADY_CREATED_EMAIL(
            HttpStatus.CONFLICT, "중복된 이메일 입니다."),


    // 500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다. 서버 팀에 연락주세요!");

    private final HttpStatus status;
    private final String message;
}
