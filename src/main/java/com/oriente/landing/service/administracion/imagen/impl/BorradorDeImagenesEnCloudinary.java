package com.oriente.landing.service.administracion.imagen.impl;

import com.oriente.landing.config.PropiedadesDeCloudinary;
import com.oriente.landing.service.administracion.imagen.BorradorDeImagenes;
import com.oriente.landing.service.administracion.imagen.ImagenesQuedaronHuerfanas;
import com.oriente.landing.util.FirmadorDeCloudinary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Borra en Cloudinary los archivos que ya no referencia ningun registro.
 *
 * Sin esto, cada foto reemplazada o eliminada queda ocupando lugar en la cuenta
 * para siempre: el registro desaparece de la base y el archivo no, y no queda
 * forma de saber cual era cual. Por eso de cada imagen se guarda su public_id.
 */
@Service
public class BorradorDeImagenesEnCloudinary implements BorradorDeImagenes {

    private static final Logger log = LoggerFactory.getLogger(BorradorDeImagenesEnCloudinary.class);

    private final PropiedadesDeCloudinary propiedades;
    private final RestClient clienteHttp;

    public BorradorDeImagenesEnCloudinary(PropiedadesDeCloudinary propiedades) {
        this.propiedades = propiedades;
        // Se crea directo en lugar de pedir el RestClient.Builder autoconfigurado:
        // en Spring Boot 4 esa autoconfiguracion vive en un modulo aparte, y este
        // cliente habla con un unico servicio externo sin nada que compartir.
        this.clienteHttp = RestClient.create("https://api.cloudinary.com");
    }

    /**
     * Se atiende despues del commit: hasta ese momento el borrado en la base
     * todavia puede deshacerse, y un archivo borrado no.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alQuedarHuerfanas(ImagenesQuedaronHuerfanas evento) {
        borrar(evento.imagenes());
        borrarVideos(evento.videos());
    }

    @Override
    public void borrar(Set<String> publicIds) {
        borrarTodos(publicIds, "image");
    }

    @Override
    public void borrarVideos(Set<String> publicIds) {
        borrarTodos(publicIds, "video");
    }

    private void borrarTodos(Set<String> publicIds, String tipoDeRecurso) {
        if (publicIds == null || publicIds.isEmpty()) {
            return;
        }

        if (!propiedades.estaConfigurado()) {
            // Se deja constancia para que los archivos se puedan limpiar a mano:
            // sin credenciales no hay forma de pedirle a Cloudinary que los borre.
            log.warn("Cloudinary no esta configurado: quedan {} archivos ({}) sin borrar {}",
                    publicIds.size(), tipoDeRecurso, publicIds);
            return;
        }

        publicIds.stream()
                .filter(publicId -> publicId != null && !publicId.isBlank())
                .forEach(publicId -> borrarUno(publicId, tipoDeRecurso));
    }

    private void borrarUno(String publicId, String tipoDeRecurso) {
        long marcaDeTiempo = Instant.now().getEpochSecond();

        Map<String, String> aFirmar = new LinkedHashMap<>();
        aFirmar.put("public_id", publicId);
        aFirmar.put("timestamp", String.valueOf(marcaDeTiempo));

        MultiValueMap<String, String> formulario = new LinkedMultiValueMap<>();
        formulario.add("public_id", publicId);
        formulario.add("timestamp", String.valueOf(marcaDeTiempo));
        formulario.add("api_key", propiedades.apiKey());
        formulario.add("signature", FirmadorDeCloudinary.firmar(aFirmar, propiedades.apiSecret()));

        try {
            Map<String, Object> respuesta = clienteHttp.post()
                    // El tipo de recurso va en la ruta: un video no se borra por el
                    // camino de las imagenes.
                    .uri("/v1_1/{nube}/{tipo}/destroy", propiedades.nombreDeLaNube(), tipoDeRecurso)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formulario)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});

            // Cloudinary contesta 200 con {"result":"not found"} cuando el archivo ya
            // no existe: no es un error, sólo significa que no habia nada que borrar.
            Object resultado = respuesta == null ? null : respuesta.get("result");
            if ("ok".equals(resultado) || "not found".equals(resultado)) {
                log.info("Archivo {} ({}) borrado de Cloudinary ({})", publicId, tipoDeRecurso, resultado);
            } else {
                log.warn("Cloudinary no borro el archivo {} ({}): {}", publicId, tipoDeRecurso, resultado);
            }

        } catch (Exception ex) {
            // El registro en la base ya se borro y eso no se deshace. Queda el aviso
            // con el public_id para poder limpiarlo despues.
            log.error("No se pudo borrar el archivo {} ({}) de Cloudinary: {}",
                    publicId, tipoDeRecurso, ex.getMessage());
        }
    }
}
