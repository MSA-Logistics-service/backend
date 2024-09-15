package msa.logistics.service.gateway.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import msa.logistics.service.gateway.client.UserServiceClient;
import msa.logistics.service.gateway.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.util.StringUtils;
import org.springframework.web.server.WebFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserServiceClient userServiceClient;

    public SecurityConfig(JwtUtil jwtUtil, UserServiceClient userServiceClient) {
        this.jwtUtil = jwtUtil;
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
            // /auth/login 경로는 필터를 적용하지 않음
            if (exchange.getRequest().getURI().getPath().equals("/api/v1/auth/sign-up")
                    || exchange.getRequest().getURI().getPath().equals("/api/v1/auth/sign-in")) {
                return chain.filter(exchange);
            }

            String tokenValue = jwtUtil.getTokenFromRequest((HttpServletRequest) exchange.getRequest());

            if (StringUtils.hasText(tokenValue)) {
                // JWT 토큰 substring
                tokenValue = jwtUtil.substringToken(tokenValue);

                if (!jwtUtil.validateToken(tokenValue)) {
                    return;
                }

                Claims claims = jwtUtil.getUserInfoFromToken(tokenValue);

                String username = claims.getSubject();
                
            }
        };
    }
}
