package com.dev.realtimechat.shared.jwt;

import lombok.Builder;
import lombok.Getter;

@Builder
public record TokenClaims(
        String id,
        String issuer,
        @Getter
        String nameTag,
        @Getter
        String randId,
        @Getter
        String ipAddress,
        String regionCode,
        long issuedAt,
        long notBefore,
        long expiresAt
) { }