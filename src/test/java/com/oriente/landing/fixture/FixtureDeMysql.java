package com.oriente.landing.fixture;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * La base de los tests: el mismo MySQL 9 que corre en desarrollo y en Railway.
 *
 * @ServiceConnection le pasa a Spring la URL y las credenciales del contenedor,
 * asi que no hay que repetirlas en un properties de test.
 */
@TestConfiguration(proxyBeanMethods = false)
public class FixtureDeMysql {

    @Bean
    @ServiceConnection
    public MySQLContainer<?> mysql() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:9"))
                .withCommand("--default-time-zone=+00:00", "--character-set-server=utf8mb4");
    }
}
