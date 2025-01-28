package com.dev.realtimechat.infra.config;

import com.dev.realtimechat.infra.security.JwtAuthenticationFilter;
import com.dev.realtimechat.infra.security.JwtAuthenticationProvider;
import com.dev.realtimechat.infra.security.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CorsConfig corsConfig;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            AuthenticationConfiguration authConfig,
            JwtProvider jwtProvider,
            ObjectMapper objectMapper) throws Exception {

        return new JwtAuthenticationFilter(authConfig.getAuthenticationManager(), jwtProvider, objectMapper);
    }


    // 요청에 대한 사용자 권한을 위한 구성 메서드
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return
                http
                        .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
                        .csrf(AbstractHttpConfigurer::disable) // REST API 는 기본적으로 상태를 유지하지 않으며, CSRF 방어가 필요하지 않기 때문에 비활성화
                        .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource())) // CORS 허용
                        // 세션 관리 설정
                        // Stateless 로 설정하여 서버가 세션 상태를 유지하지 않음 (JWT 인증에 적합)
                        .sessionManagement(configurer ->
                                configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/chatroom/**").authenticated() // 채팅방 crud 인증 필요
                                .requestMatchers("/chat").authenticated() // WebSocket Handshake 경로에 인증 적용
                                .requestMatchers("/pub/**").authenticated() // 메시지 전송 경로 인증
                                .requestMatchers("/sub/**").permitAll() // 메시지 구독 경로 인증
                                .anyRequest().authenticated()
                        )
                        // JWT 인증처리
                        .authenticationProvider(jwtAuthenticationProvider)

                        // 커스텀 인증 필터 추가
                        // `UsernamePasswordAuthenticationFilter` 전에 `JwtAuthenticationFilter` 실행
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                        // 특정 경로에만 이 보안 설정 적용
                        .securityMatcher("/chatroom/**", "/init", "/message")

                        // SecurityFilterChain 빌드
                        .build();
    }
}
