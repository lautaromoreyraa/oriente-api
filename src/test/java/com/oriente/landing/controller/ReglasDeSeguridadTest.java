package com.oriente.landing.controller;

import com.oriente.landing.fixture.FixtureDeMysql;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Un caso por regla de seguridad.
 *
 * La configuracion anterior cerraba con anyRequest().permitAll(), y un endpoint
 * nuevo fuera del prefijo esperado quedaba publico sin que nadie lo decidiera.
 * Estos tests existen para que ese tipo de descuido falle en el build y no en
 * produccion.
 */
@SpringBootTest(properties = {
        "oriente.seguridad.administrador.usuario=admin-de-test",
        "oriente.seguridad.administrador.password=password-de-test",
        "oriente.seguridad.jwt.secret=secreto-de-test-largo-para-hmac-sha256-que-necesita-al-menos-32-bytes"
})
@AutoConfigureMockMvc
@Import(FixtureDeMysql.class)
class ReglasDeSeguridadTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String CREDENCIALES_VALIDAS =
            "{\"usuario\":\"admin-de-test\",\"password\":\"password-de-test\"}";

    private String tokenValido() throws Exception {
        MvcResult resultado = mockMvc.perform(post("/api/admin/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREDENCIALES_VALIDAS))
                .andExpect(status().isOk())
                .andReturn();

        String cuerpo = resultado.getResponse().getContentAsString();
        return cuerpo.replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");
    }

    // ── Superficie publica ────────────────────────────────────────────

    @Test
    @DisplayName("la landing se lee sin autenticacion")
    void laLandingEsPublica() throws Exception {
        mockMvc.perform(get("/api/publico/v1/landing"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("la landing sale con Cache-Control para que el borde la guarde")
    void laLandingSeCachea() throws Exception {
        mockMvc.perform(get("/api/publico/v1/landing"))
                .andExpect(status().isOk())
                .andExpect(resultado -> {
                    String cacheControl = resultado.getResponse().getHeader("Cache-Control");
                    if (cacheControl == null || !cacheControl.contains("max-age")) {
                        throw new AssertionError("Falta Cache-Control: " + cacheControl);
                    }
                });
    }

    @Test
    @DisplayName("no se puede escribir en la superficie publica, ni con token")
    void laSuperficiePublicaEsSoloLectura() throws Exception {
        mockMvc.perform(post("/api/publico/v1/landing"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/publico/v1/landing")
                        .header("Authorization", "Bearer " + tokenValido()))
                .andExpect(status().isForbidden());
    }

    // ── Superficie de administracion ──────────────────────────────────

    @Test
    @DisplayName("sin token, administracion responde 401 y no 403: el panel necesita distinguirlos")
    void administracionSinTokenDa401() throws Exception {
        mockMvc.perform(get("/api/admin/v1/servicios"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/api/admin/v1/hero")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Intento sin token\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("un token inventado no pasa")
    void tokenInvalidoNoPasa() throws Exception {
        mockMvc.perform(get("/api/admin/v1/servicios")
                        .header("Authorization", "Bearer esto.no.es.un.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("hasta las lecturas de administracion piden token")
    void lasLecturasDeAdministracionNoSonPublicas() throws Exception {
        for (String ruta : new String[]{"/api/admin/v1/servicios", "/api/admin/v1/combos",
                "/api/admin/v1/hero", "/api/admin/v1/nosotros",
                "/api/admin/v1/contacto", "/api/admin/v1/instagram"}) {
            mockMvc.perform(get(ruta)).andExpect(status().isUnauthorized());
        }
    }

    @Test
    @DisplayName("con token valido se entra")
    void conTokenValidoSeEntra() throws Exception {
        mockMvc.perform(get("/api/admin/v1/servicios")
                        .header("Authorization", "Bearer " + tokenValido()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("credenciales incorrectas dan 401, no 500")
    void credencialesIncorrectas() throws Exception {
        mockMvc.perform(post("/api/admin/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"admin-de-test\",\"password\":\"la-que-no-es\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.estado").value(401));
    }

    @Test
    @DisplayName("un JSON roto da 400 y no un error interno")
    void cuerpoIlegibleDa400() throws Exception {
        mockMvc.perform(post("/api/admin/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{esto no es json"))
                .andExpect(status().isBadRequest());
    }

    // ── Todo lo demas ─────────────────────────────────────────────────

    @Test
    @DisplayName("una ruta que no cae en ninguna cadena queda cerrada")
    void loQueNoEstaDeclaradoSeRechaza() throws Exception {
        mockMvc.perform(get("/actuator/env")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/services")).andExpect(status().isForbidden());
        mockMvc.perform(get("/")).andExpect(status().isForbidden());
    }
}
