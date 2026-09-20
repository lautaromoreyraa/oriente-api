package com.oriente.landing.controller;

import com.oriente.landing.fixture.FixtureDeMysql;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica que las migraciones dejan la landing servible desde el primer arranque
 * y que el endpoint agregado devuelve las secciones que el frontend espera.
 */
@SpringBootTest(properties = {
        "oriente.seguridad.administrador.usuario=admin-de-test",
        "oriente.seguridad.administrador.password=password-de-test",
        "oriente.seguridad.jwt.secret=secreto-de-test-largo-para-hmac-sha256-que-necesita-al-menos-32-bytes"
})
@AutoConfigureMockMvc
@Import(FixtureDeMysql.class)
class ContenidoDeLandingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("los datos iniciales de las migraciones alcanzan para armar la pagina")
    void laLandingTraeContenido() throws Exception {
        mockMvc.perform(get("/api/publico/v1/landing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hero.titulo").value("Oriente"))
                .andExpect(jsonPath("$.servicios.length()").value(9))
                .andExpect(jsonPath("$.combos.length()").value(4))
                .andExpect(jsonPath("$.combos[0].incluye.length()").value(3))
                .andExpect(jsonPath("$.nosotros.diferenciales.length()").value(4))
                .andExpect(jsonPath("$.contacto.instagram").value("oriente.rcia"));
    }

    @Test
    @DisplayName("los acentos sobreviven al viaje por la base")
    void losAcentosLleganBien() throws Exception {
        mockMvc.perform(get("/api/publico/v1/servicios/kinesiologia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Kinesiología"));
    }

    @Test
    @DisplayName("un slug que no existe da 404")
    void slugInexistente() throws Exception {
        mockMvc.perform(get("/api/publico/v1/servicios/no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value(404));
    }
}
