package com.dev.realtimechat.infra.security;

public interface JwtProperties {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
}