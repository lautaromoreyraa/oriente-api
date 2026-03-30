package com.oriente.landing.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String requestPath = request.getRequestURI();

        try {
            // Si es un endpoint protegido y no tiene token, rechazar
            if (isProtectedEndpoint(request) && (authHeader == null || !authHeader.startsWith("Bearer "))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Token JWT requerido\"}");
                return;
            }

            // Si tiene token, validarlo
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtUtil.isTokenValid(token)) {
                    String username = jwtUtil.extractUsername(token);

                    // Crear lista de authorities/permisos
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Token validado para usuario: {}", username);
                } else {
                    // Token inválido
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"Token JWT inválido o expirado\"}");
                    return;
                }
            }
        } catch (Exception e) {
            log.debug("Error procesando JWT: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Error procesando token JWT\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    //Determina si un endpoint requiere autenticación JWT

    private boolean isProtectedEndpoint(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();

        // GET y POST /auth/login son públicos
        if (path.equals("/api/v1/auth/login")) {
            return false;
        }

        /* GET es público para todo /api/v1/**
          Esto permite que los endpoints GET sean accesibles sin autenticación,
          mientras que POST, PUT, DELETE en /api/v1/** requerirán un token JWT válido.
         */
        if ("GET".equalsIgnoreCase(method)) {
            return false;
        }

        // POST, PUT, DELETE en /api/v1/** requieren token
        return path.startsWith("/api/v1/");
    }
}
