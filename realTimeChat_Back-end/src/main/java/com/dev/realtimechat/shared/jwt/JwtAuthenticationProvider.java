package com.dev.realtimechat.shared.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtProvider jwtProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            throw new IllegalArgumentException("Unsupported authentication type");
        }

        JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
        String token = (String) jwtAuthentication.getCredentials();
        String ipAddress = jwtAuthentication.getIpAddress();

        try {
            log.info("유효성 검사 실시");
            TokenClaims claims = jwtProvider.validateToken(token, ipAddress);
            return new JwtAuthenticationToken(claims, ipAddress, Collections.emptyList());
        }
        catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid JWT token", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}