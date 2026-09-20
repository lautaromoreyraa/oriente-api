package com.oriente.landing.service.administracion.imagen.impl;

import com.oriente.landing.config.PropiedadesDeCloudinary;
import com.oriente.landing.dto.administracion.imagen.FirmaDeUploadResponse;
import com.oriente.landing.exception.ConfiguracionIncompletaException;
import com.oriente.landing.service.administracion.imagen.FirmaDeUploadService;
import com.oriente.landing.util.FirmadorDeCloudinary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FirmaDeUploadServiceImpl implements FirmaDeUploadService {

    private final PropiedadesDeCloudinary propiedades;

    public FirmaDeUploadServiceImpl(PropiedadesDeCloudinary propiedades) {
        this.propiedades = propiedades;
    }

    @Override
    public FirmaDeUploadResponse generarFirma() {
        if (!propiedades.estaConfigurado()) {
            throw new ConfiguracionIncompletaException(
                    "Cloudinary no esta configurado en el servidor: faltan CLOUDINARY_CLOUD_NAME, "
                            + "CLOUDINARY_API_KEY o CLOUDINARY_API_SECRET");
        }

        long marcaDeTiempo = Instant.now().getEpochSecond();

        // Solo se firman folder y timestamp, y son los unicos parametros que el panel
        // puede mandar: si tambien pudiera elegir el public_id o transformaciones,
        // la firma le dejaria sobrescribir imagenes existentes.
        Map<String, String> parametros = new LinkedHashMap<>();
        parametros.put("folder", propiedades.carpeta());
        parametros.put("timestamp", String.valueOf(marcaDeTiempo));

        String firma = FirmadorDeCloudinary.firmar(parametros, propiedades.apiSecret());

        return new FirmaDeUploadResponse(
                firma,
                marcaDeTiempo,
                propiedades.apiKey(),
                propiedades.nombreDeLaNube(),
                propiedades.carpeta()
        );
    }
}
