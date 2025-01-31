package com.dev.realtimechat.infra.security;

import com.dev.realtimechat.application.api.ApiResponse;
import com.dev.realtimechat.application.type.ErrorType;
import com.dev.realtimechat.application.type.SuccessType;
import com.dev.realtimechat.infra.utils.IpAddressUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Set<String> SECURED_API_PATHS = Set.of(
            "/chatroom/**", "/init", "/message"
    );

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveJwtToken(request);
        String ipAddress = IpAddressUtil.extractIpAddress(request); // Client IP


        // Case the token doesn't exist
        if (token == null || token.equals("null")) {
            handleNoToken(ipAddress, response);
            return;
        }

        //Case the token exists
        try {
            JwtAuthenticationToken authRequest = new JwtAuthenticationToken(token, ipAddress);
            // JwtTokenProvider 로 위임
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleExpiredToken(ipAddress, response);
        } catch (Exception e) {
            handleInvalidToken(response, "INVALID_TOKEN");
        }
    }

    //if the token doesn't exist, publish new token.
    private void handleNoToken(String ipAddress, HttpServletResponse response) throws IOException {
        String newToken = jwtProvider.generateToken(ipAddress);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), ApiResponse.success(
                newToken,
                SuccessType.SUCCESS_TOKEN_ISSUANCE));
    }

    // if the token has expired, publish new token.
    private void handleExpiredToken(String ipAddress, HttpServletResponse response) throws IOException {
        String newToken = jwtProvider.generateToken(ipAddress);
        objectMapper.writeValue(response.getWriter(), ApiResponse.error(
                newToken,
                "EXPIRED_TOKEN",
                ErrorType.EXPIRED_TOKEN));
    }

    // if the token is failed to validate
    private void handleInvalidToken(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), ApiResponse.error(
                message,
                ErrorType.INVALID_TOKEN));
    }

    // 토큰을 담아 response
    private void sendTokenResponse(HttpServletResponse response, String token) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), ApiResponse.success(
                token,
                SuccessType.SUCCESS_TOKEN_ISSUANCE));
    }

    private String resolveJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(JwtProperties.HEADER_STRING);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return authorizationHeader.substring(JwtProperties.TOKEN_PREFIX.length());
        }

        return null;
    }


    //SECURED_API_PATHS 를 제외한 모든 경로는 Filter 적용 X
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return SECURED_API_PATHS.stream()
                .noneMatch(pattern -> antPathMatcher.match(pattern, path));
    }
}