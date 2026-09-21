package com.oriente.landing.service.administracion.imagen.impl;

import com.oriente.landing.config.PropiedadesDeCloudinary;
import com.oriente.landing.exception.ConfiguracionIncompletaException;
import com.oriente.landing.exception.ServicioExternoException;
import com.oriente.landing.service.administracion.imagen.SubidorDeArchivos;
import com.oriente.landing.util.FirmadorDeCloudinary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class SubidorDeArchivosACloudinary implements SubidorDeArchivos {

    /**
     * Con una URL de origen, Cloudinary responde recien cuando termino de bajar el
     * archivo. Un reel de un minuto tarda unos segundos; el margen es para no
     * cortar uno largo a la mitad.
     */
    private static final Duration ESPERA_MAXIMA = Duration.ofMinutes(2);

    private final PropiedadesDeCloudinary propiedades;
    private final RestClient clienteHttp;

    public SubidorDeArchivosACloudinary(PropiedadesDeCloudinary propiedades) {
        this.propiedades = propiedades;

        JdkClientHttpRequestFactory fabrica = new JdkClientHttpRequestFactory(
                HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build());
        fabrica.setReadTimeout(ESPERA_MAXIMA);

        this.clienteHttp = RestClient.builder()
                .baseUrl("https://api.cloudinary.com")
                .requestFactory(fabrica)
                .build();
    }

    @Override
    public ArchivoSubido subirImagen(String urlDeOrigen) {
        return subir(urlDeOrigen, "image");
    }

    @Override
    public ArchivoSubido subirVideo(String urlDeOrigen) {
        return subir(urlDeOrigen, "video");
    }

    private ArchivoSubido subir(String urlDeOrigen, String tipoDeRecurso) {
        if (!propiedades.estaConfigurado()) {
            throw new ConfiguracionIncompletaException(
                    "Cloudinary no esta configurado en el servidor: faltan CLOUDINARY_CLOUD_NAME, "
                            + "CLOUDINARY_API_KEY o CLOUDINARY_API_SECRET");
        }

        String marcaDeTiempo = String.valueOf(Instant.now().getEpochSecond());

        Map<String, String> aFirmar = new LinkedHashMap<>();
        aFirmar.put("folder", propiedades.carpeta());
        aFirmar.put("timestamp", marcaDeTiempo);

        MultiValueMap<String, String> formulario = new LinkedMultiValueMap<>();
        formulario.add("file", urlDeOrigen);
        formulario.add("folder", propiedades.carpeta());
        formulario.add("timestamp", marcaDeTiempo);
        formulario.add("api_key", propiedades.apiKey());
        formulario.add("signature", FirmadorDeCloudinary.firmar(aFirmar, propiedades.apiSecret()));

        Map<String, Object> respuesta;
        try {
            respuesta = clienteHttp.post()
                    .uri("/v1_1/{nube}/{tipo}/upload", propiedades.nombreDeLaNube(), tipoDeRecurso)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formulario)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});
        } catch (Exception ex) {
            throw new ServicioExternoException("Cloudinary no pudo guardar el archivo", ex);
        }

        if (respuesta == null || respuesta.get("secure_url") == null || respuesta.get("public_id") == null) {
            throw new ServicioExternoException("Cloudinary no devolvio el archivo guardado");
        }
        return new ArchivoSubido(
                respuesta.get("secure_url").toString(),
                respuesta.get("public_id").toString());
    }
}
