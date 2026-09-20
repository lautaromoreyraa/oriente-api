package com.oriente.landing.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/** Emite y verifica los tokens del panel. */
@Component
public class ProveedorDeTokensJwt {

    private static final Logger log = LoggerFactory.getLogger(ProveedorDeTokensJwt.class);

    private final SecretKey clave;
    private final long expiracionEnMilisegundos;

    public ProveedorDeTokensJwt(PropiedadesDeSeguridad propiedades) {
        this.clave = Keys.hmacShaKeyFor(propiedades.jwt().secret().getBytes(StandardCharsets.UTF_8));
        this.expiracionEnMilisegundos = propiedades.jwt().expiracionEnMilisegundos();
    }

    public String generarPara(String usuario) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(usuario)
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expiracionEnMilisegundos))
                .signWith(clave)
                .compact();
    }

    /**
     * Devuelve el usuario del token, o null si el token no sirve.
     *
     * Verificar y extraer en un solo paso evita el patron de "validar y despues
     * volver a parsear", donde el segundo parseo puede ver algo distinto al primero.
     */
    public String usuarioSiEsValido(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(clave)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("Token rechazado: {}", ex.getMessage());
            return null;
        }
    }

    public long getExpiracionEnMilisegundos() {
        return expiracionEnMilisegundos;
    }
}
