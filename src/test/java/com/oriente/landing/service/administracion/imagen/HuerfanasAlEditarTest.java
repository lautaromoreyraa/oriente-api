package com.oriente.landing.service.administracion.imagen;

import com.oriente.landing.dto.administracion.servicio.ImagenDeServicioRequest;
import com.oriente.landing.dto.administracion.servicio.ServicioRequest;
import com.oriente.landing.dto.administracion.servicio.ServicioResponse;
import com.oriente.landing.fixture.FixtureDeMysql;
import com.oriente.landing.service.administracion.servicio.CatalogoDeServiciosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica que editar o borrar contenido avise exactamente que archivos quedaron
 * sin referencia.
 *
 * El test escucha el evento en lugar de hablar con Cloudinary: lo que importa es
 * que se avise lo correcto y despues del commit. El listener real, que es el que
 * llama al proveedor, escucha en la misma fase.
 */
@SpringBootTest(properties = {
        "oriente.seguridad.administrador.usuario=admin-de-test",
        "oriente.seguridad.administrador.password=password-de-test",
        "oriente.seguridad.jwt.secret=secreto-de-test-largo-para-hmac-sha256-que-necesita-al-menos-32-bytes"
})
@Import({FixtureDeMysql.class, HuerfanasAlEditarTest.EspiaDeEventos.class})
class HuerfanasAlEditarTest {

    @Component
    static class EspiaDeEventos {
        final List<Set<String>> recibidos = new ArrayList<>();

        // Misma fase que el borrador real: si la transaccion no commitea, no llega.
        @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
        public void alQuedarHuerfanas(ImagenesQuedaronHuerfanas evento) {
            recibidos.add(evento.imagenes());
        }
    }

    @Autowired
    private CatalogoDeServiciosService catalogo;

    @Autowired
    private EspiaDeEventos espia;

    @BeforeEach
    void limpiarLoEscuchado() {
        espia.recibidos.clear();
    }

    private ServicioRequest conImagenes(String titulo, String principal, String... delCarrusel) {
        List<ImagenDeServicioRequest> imagenes = new ArrayList<>();
        for (String publicId : delCarrusel) {
            imagenes.add(new ImagenDeServicioRequest(
                    "https://res.cloudinary.com/demo/image/upload/" + publicId + ".jpg",
                    publicId, "una foto", null, null, true));
        }

        return new ServicioRequest(
                null, "KINE", titulo, "descripcion", null,
                principal == null ? null : "https://res.cloudinary.com/demo/image/upload/" + principal + ".jpg",
                principal, "principal", 0, true, imagenes);
    }

    private String unNombreUnico() {
        return "Servicio " + UUID.randomUUID();
    }

    @Test
    @DisplayName("quitar una foto del carrusel avisa que ese archivo quedo sin referencia")
    void alQuitarUnaFotoAvisa() {
        ServicioResponse creado = catalogo.crear(
                conImagenes(unNombreUnico(), "oriente/principal-1", "oriente/foto-a", "oriente/foto-b"));
        espia.recibidos.clear();

        catalogo.actualizar(creado.id(),
                conImagenes(creado.titulo(), "oriente/principal-1", "oriente/foto-a"));

        assertEquals(1, espia.recibidos.size(), "tendria que haber avisado una vez");
        assertEquals(Set.of("oriente/foto-b"), espia.recibidos.get(0));
    }

    @Test
    @DisplayName("reemplazar la foto principal avisa por la anterior, no por la nueva")
    void alReemplazarLaPrincipalAvisaPorLaVieja() {
        ServicioResponse creado = catalogo.crear(conImagenes(unNombreUnico(), "oriente/vieja"));
        espia.recibidos.clear();

        catalogo.actualizar(creado.id(), conImagenes(creado.titulo(), "oriente/nueva"));

        assertEquals(Set.of("oriente/vieja"), espia.recibidos.get(0));
    }

    @Test
    @DisplayName("editar sin tocar las fotos no manda a borrar nada")
    void editarElTextoNoBorraFotos() {
        ServicioResponse creado = catalogo.crear(
                conImagenes(unNombreUnico(), "oriente/principal-2", "oriente/foto-c"));
        espia.recibidos.clear();

        catalogo.actualizar(creado.id(),
                conImagenes(creado.titulo(), "oriente/principal-2", "oriente/foto-c"));

        assertTrue(espia.recibidos.get(0).isEmpty(), "no habia nada que borrar");
    }

    @Test
    @DisplayName("borrar el servicio avisa por todas sus fotos")
    void alBorrarElServicioAvisaPorTodas() {
        ServicioResponse creado = catalogo.crear(
                conImagenes(unNombreUnico(), "oriente/principal-3", "oriente/foto-d", "oriente/foto-e"));
        espia.recibidos.clear();

        catalogo.eliminar(creado.id());

        assertEquals(
                Set.of("oriente/principal-3", "oriente/foto-d", "oriente/foto-e"),
                espia.recibidos.get(0));
    }
}
