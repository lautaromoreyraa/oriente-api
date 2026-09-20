package com.oriente.landing.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalDeErrores {

    private static final Logger log = LoggerFactory.getLogger(ManejadorGlobalDeErrores.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<RespuestaDeError> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RespuestaDeError.de(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(ReglaDeNegocioException.class)
    public ResponseEntity<RespuestaDeError> manejarReglaDeNegocio(ReglaDeNegocioException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(RespuestaDeError.de(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(ConfiguracionIncompletaException.class)
    public ResponseEntity<RespuestaDeError> manejarConfiguracionIncompleta(ConfiguracionIncompletaException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(RespuestaDeError.de(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaDeError> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(RespuestaDeError.deValidacion(
                "Errores de validacion", HttpStatus.BAD_REQUEST.value(), errores));
    }

    /**
     * El cuerpo no se pudo leer: JSON mal formado, vacio, o un tipo que no encaja.
     * Es un error de quien llama, no del servidor, asi que va 400 y no 500.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespuestaDeError> manejarCuerpoIlegible(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(RespuestaDeError.de(
                "El cuerpo del pedido no es JSON valido", HttpStatus.BAD_REQUEST.value()));
    }

    /** Un id que no es un numero, por ejemplo /servicios/abc. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RespuestaDeError> manejarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(RespuestaDeError.de(
                "El parametro '" + ex.getName() + "' tiene un formato invalido", HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<RespuestaDeError> manejarNoAutenticado(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(RespuestaDeError.de("No autenticado", HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RespuestaDeError> manejarAccesoDenegado(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(RespuestaDeError.de("Acceso denegado", HttpStatus.FORBIDDEN.value()));
    }

    /**
     * Ultimo recurso. El mensaje de la excepcion no se devuelve: puede contener
     * nombres de tabla, SQL o rutas internas. Al log si va completo.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaDeError> manejarErrorInesperado(Exception ex) {
        log.error("Error inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RespuestaDeError.de("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
