package com.dev.realtimechat.shared.jwt;

import com.dev.realtimechat.chatroom.domain.Champion;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
public class JwtProvider {
    private final SecretKey key;
    private final String serverIdentifier;
    private final long expirationSeconds;
    private final JwtParser jwtParser;

    public JwtProvider(
            @Value("${token.secret}") String secret,
            @Value("${token.server-ip}") String serverAddress,
            @Value("${token.token-expiration}") long expirationSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.serverIdentifier = generateServerIdentifier(serverAddress, secret);
        this.expirationSeconds = expirationSeconds;
        this.jwtParser = Jwts.parser().verifyWith(key).build();
    }

    public String generateToken(String ipAddress) {
        Instant now = Instant.now();

        TokenClaims claims = TokenClaims.builder()
                .id(UUID.randomUUID().toString())
                .issuer(serverIdentifier)
                .nameTag(generateNameTag())
                .randId(generateRandId())
                .ipAddress(ipAddress)
                .issuedAt(now.getEpochSecond())
                .notBefore(now.minusSeconds(600).getEpochSecond())
                .expiresAt(now.plusSeconds(expirationSeconds).getEpochSecond())
                .build();


        return Jwts.builder()
                .id(claims.id())
                .issuer(claims.issuer())
                .claim("randId", claims.randId())
                .claim("regionCode", claims.regionCode())
                .audience().add(claims.ipAddress()).and()
                .subject(claims.nameTag())
                .issuedAt(Date.from(Instant.ofEpochSecond(claims.issuedAt())))
                .notBefore(Date.from(Instant.ofEpochSecond(claims.notBefore())))
                .expiration(Date.from(Instant.ofEpochSecond(claims.expiresAt())))
                .signWith(key)
                .compact();
    }


    public String generateRandId() {
        Random random = new Random();
        Champion[] champions  = Champion.values();
        Champion randomChampion = champions[random.nextInt(champions.length)];

        int randNum = random.nextInt(99) + 1;  // 1부터 99까지의 숫자 생성

        return randomChampion.toString() + randNum;
    }

    public String generateNameTag() {
        // UUID 를 생성하고, 이를 16진수로 변환 후, 대문자로 변경
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        // UUID 에서 필요한 길이만큼 자르고, #을 앞에 붙여서 nameTag 로 생성
        return "#" + uuid.substring(0, 6); // 예: #YQTQJG
    }

    public TokenClaims getTokenClaims(String token) {
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        return TokenClaims.builder()
                .id(claims.getId())
                .issuer(claims.getIssuer())
                .nameTag(claims.getSubject())
                .randId(claims.get("randId", String.class))
                .ipAddress(claims.getAudience().iterator().next())
                .issuedAt(claims.getIssuedAt().getTime() / 1000)
                .notBefore(claims.getNotBefore().getTime() / 1000)
                .expiresAt(claims.getExpiration().getTime() / 1000)
                .build();
    }


    public TokenClaims validateToken(String token, String ipAddress) {
        try {
            Claims claims = jwtParser
                    .parseSignedClaims(token)
                    .getPayload();

            // Validate issuer and audience
            if (!claims.getIssuer().equals(serverIdentifier) ||
                    !claims.getAudience().contains(ipAddress)) {
                throw new JwtException.InvalidTokenException("Invalid token claims");
            }

            return TokenClaims.builder()
                    .id(claims.getId())
                    .issuer(claims.getIssuer())
                    .ipAddress(claims.getAudience().iterator().next())
                    .regionCode(claims.getSubject())
                    .issuedAt(claims.getIssuedAt().toInstant().getEpochSecond())
                    .notBefore(claims.getNotBefore().toInstant().getEpochSecond())
                    .expiresAt(claims.getExpiration().toInstant().getEpochSecond())
                    .build();

        } catch (ExpiredJwtException e) {
            throw e;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            throw new JwtException.InvalidTokenException("Invalid JWT token", e);
        }
    }

    private String generateServerIdentifier(String serverAddress, String secret) {
        return UUID.nameUUIDFromBytes(
                (serverAddress + secret).getBytes(StandardCharsets.UTF_8)
        ).toString();
    }
}
