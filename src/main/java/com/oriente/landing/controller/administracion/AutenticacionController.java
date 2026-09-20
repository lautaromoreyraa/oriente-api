package com.oriente.landing.controller.administracion;

import com.oriente.landing.config.ProveedorDeTokensJwt;
import com.oriente.landing.dto.administracion.autenticacion.LoginRequest;
import com.oriente.landing.dto.administracion.autenticacion.LoginResponse;
import com.oriente.landing.exception.RespuestaDeError;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/auth")
public class AutenticacionController {

    private static final Logger log = LoggerFactory.getLogger(AutenticacionController.class);

    private final AuthenticationManager gestorDeAutenticacion;
    private final ProveedorDeTokensJwt proveedorDeTokens;

    public AutenticacionController(AuthenticationManager gestorDeAutenticacion,
                                   ProveedorDeTokensJwt proveedorDeTokens) {
        this.gestorDeAutenticacion = gestorDeAutenticacion;
        this.proveedorDeTokens = proveedorDeTokens;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            gestorDeAutenticacion.authenticate(
                    new UsernamePasswordAuthenticationToken(request.usuario(), request.password()));

            String token = proveedorDeTokens.generarPara(request.usuario());
            log.info("Login correcto");

            return ResponseEntity.ok(new LoginResponse(
                    token,
                    proveedorDeTokens.getExpiracionEnMilisegundos(),
                    request.usuario()));

        } catch (AuthenticationException ex) {
            // El log no dice que usuario se intento: es un dato que, junto con la hora,
            // le sirve mas a quien prueba credenciales que a quien lee el log.
            log.warn("Intento de login fallido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(RespuestaDeError.de("Credenciales invalidas", HttpStatus.UNAUTHORIZED.value()));
        }
    }
}
