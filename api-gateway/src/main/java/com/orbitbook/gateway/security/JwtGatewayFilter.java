package com.orbitbook.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtGatewayFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS =
            List.of("/auth/**");

    private static final String BEARER_PREFIX = "Bearer ";

    private final SecretKey secretKey;
    private final AntPathMatcher pathMatcher =
            new AntPathMatcher();

    public JwtGatewayFilter(
            @Value("${jwt.secret}") String secret) {

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        String path = request.getServletPath();

        return PUBLIC_PATHS.stream()
                .anyMatch(pattern ->
                        pathMatcher.match(pattern, path)
                );
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null
                || !authHeader.startsWith(BEARER_PREFIX)) {

            response.setStatus(
                    HttpStatus.UNAUTHORIZED.value()
            );
            response.getWriter().write(
                    "Token JWT ausente ou mal formatado."
            );
            return;
        }

        String token =
                authHeader.substring(BEARER_PREFIX.length());

        try {

            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            Map<String, String> extraHeaders = new HashMap<>();
            extraHeaders.put("X-User-Id", userId);
            if (role != null) {
                extraHeaders.put("X-User-Role", role);
            }

            filterChain.doFilter(
                    new HeaderAddingWrapper(request, extraHeaders),
                    response
            );

        } catch (JwtException ex) {

            response.setStatus(
                    HttpStatus.UNAUTHORIZED.value()
            );
            response.getWriter().write(
                    "Token JWT inválido ou expirado."
            );
        }
    }

    private static class HeaderAddingWrapper
            extends jakarta.servlet.http.HttpServletRequestWrapper {

        private final Map<String, String> extraHeaders;

        HeaderAddingWrapper(
                HttpServletRequest request,
                Map<String, String> extraHeaders) {
            super(request);
            this.extraHeaders = extraHeaders;
        }

        @Override
        public String getHeader(String name) {
            String v = extraHeaders.get(name);
            return v != null ? v : super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            String v = extraHeaders.get(name);
            if (v != null)
                return Collections.enumeration(
                        Collections.singletonList(v)
                );
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names =
                    Collections.list(super.getHeaderNames());
            extraHeaders.keySet().forEach(k -> {
                if (!names.contains(k)) names.add(k);
            });
            return Collections.enumeration(names);
        }
    }
}
