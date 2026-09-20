package com.oriente.landing.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Traduce el header Authorization en una autenticacion de Spring Security.
 *
 * Este filtro NO rechaza pedidos: solo autentica al que trae un token valido y
 * deja pasar. Quien decide si un endpoint necesita autenticacion es la cadena de
 * seguridad. El filtro anterior tomaba esa decision por su cuenta, con su propia
 * lista de rutas publicas, y esa lista tenia que quedar sincronizada a mano con
 * la de la configuracion: dos fuentes de verdad para lo mismo.
 */
@Component
public class FiltroDeAutenticacionJwt extends OncePerRequestFilter {

    private static final String PREFIJO_BEARER = "Bearer ";

    private final ProveedorDeTokensJwt proveedorDeTokens;

    public FiltroDeAutenticacionJwt(ProveedorDeTokensJwt proveedorDeTokens) {
        this.proveedorDeTokens = proveedorDeTokens;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith(PREFIJO_BEARER)) {
            String token = header.substring(PREFIJO_BEARER.length());
            String usuario = proveedorDeTokens.usuarioSiEsValido(token);

            if (usuario != null) {
                var autenticacion = new UsernamePasswordAuthenticationToken(
                        usuario, null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            }
        }

        filterChain.doFilter(request, response);
    }
}
