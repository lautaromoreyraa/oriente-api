package com.oriente.landing.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * El unico usuario del sistema es la administradora, y sus credenciales vienen
 * del entorno. Cuando entre la base de pacientes habra que mover esto a una tabla
 * de usuarios; hasta entonces, una tabla con una sola fila seria mas ceremonia que
 * beneficio.
 */
@Component
public class DetallesDelAdministrador implements UserDetailsService {

    private final String usuario;
    private final String passwordCodificada;

    public DetallesDelAdministrador(PropiedadesDeSeguridad propiedades, PasswordEncoder codificador) {
        this.usuario = propiedades.administrador().usuario();
        // Se codifica una sola vez al arrancar: BCrypt es deliberadamente lento y
        // hacerlo en cada intento de login regalaria una forma de saturar el servidor.
        this.passwordCodificada = codificador.encode(propiedades.administrador().password());
    }

    @Override
    public UserDetails loadUserByUsername(String nombreDeUsuario) throws UsernameNotFoundException {
        if (!usuario.equals(nombreDeUsuario)) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        return User.builder()
                .username(usuario)
                .password(passwordCodificada)
                .roles("ADMIN")
                .build();
    }
}
