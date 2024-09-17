package msa.logistics.service.gateway.config;

import io.jsonwebtoken.Claims;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import msa.logistics.service.gateway.client.UserServiceClient;
import msa.logistics.service.gateway.dto.UserDto;
import msa.logistics.service.gateway.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserServiceClient userServiceClient;

    public SecurityConfig(JwtUtil jwtUtil, UserServiceClient userServiceClient) {
        this.jwtUtil = jwtUtil;
        this.userServiceClient = userServiceClient;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // CSRF 비활성화
                .addFilterAt(jwtAuthenticationFilter(jwtUtil), SecurityWebFiltersOrder.HTTP_BASIC);

        return http.build();
    }

    @Bean
    public WebFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
        return (exchange, chain) -> {

            log.info("check filter run");

            // /auth/login 경로는 필터를 적용하지 않음
            if (exchange.getRequest().getURI().getPath().equals("/api/v1/auth/sign-up")
                    || exchange.getRequest().getURI().getPath().equals("/api/v1/auth/sign-in")) {
                return chain.filter(exchange);
            }

            String tokenValue = jwtUtil.getTokenFromRequest(exchange.getRequest());

            if (StringUtils.hasText(tokenValue)) {
                // JWT 토큰 substring
                tokenValue = jwtUtil.substringToken(tokenValue);

                if (!jwtUtil.validateToken(tokenValue)) {
                    return chain.filter(exchange);
                }

                Claims claims = jwtUtil.getUserInfoFromToken(tokenValue);

                String username = claims.getSubject();

                UserDto userDto = Optional.ofNullable(userServiceClient.getUserByUsername(username))
                        .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

                // 사용자 정보를 새로운 헤더에 추가
                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-User-Name", userDto.getUsername())  // 사용자명 헤더 추가
                        .header("X-User-Roles", String.join(",", userDto.getRoles()))    // 권한 정보 헤더 추가
                        .build();

                // 수정된 요청으로 필터 체인 계속 처리
                ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
                return chain.filter(modifiedExchange);
            }
            return chain.filter(exchange);
        };
    }
}
