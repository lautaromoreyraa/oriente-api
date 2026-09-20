package com.oriente.landing.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Calcula la firma que Cloudinary exige para un upload autenticado. Sin estado.
 *
 * Cloudinary espera los parametros ordenados alfabeticamente, concatenados como
 * clave=valor separados por &, con el api_secret pegado al final, y todo eso
 * hasheado en SHA-1. El orden no es un detalle: si cambia, la firma no valida.
 */
public final class FirmadorDeCloudinary {

    private FirmadorDeCloudinary() {
    }

    public static String firmar(Map<String, String> parametros, String apiSecret) {
        String cadena = parametros.entrySet().stream()
                .filter(parametro -> parametro.getValue() != null && !parametro.getValue().isBlank())
                .sorted(Map.Entry.comparingByKey())
                .map(parametro -> parametro.getKey() + "=" + parametro.getValue())
                .collect(Collectors.joining("&"));

        return enSha1Hexadecimal(cadena + apiSecret);
    }

    private static String enSha1Hexadecimal(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] resumen = digest.digest(texto.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexadecimal = new StringBuilder(resumen.length * 2);
            for (byte octeto : resumen) {
                hexadecimal.append(Character.forDigit((octeto >> 4) & 0xF, 16));
                hexadecimal.append(Character.forDigit(octeto & 0xF, 16));
            }
            return hexadecimal.toString();
        } catch (NoSuchAlgorithmException ex) {
            // SHA-1 es parte de la especificacion de la JVM: si falta, el entorno esta roto.
            throw new IllegalStateException("La JVM no tiene SHA-1 disponible", ex);
        }
    }
}
