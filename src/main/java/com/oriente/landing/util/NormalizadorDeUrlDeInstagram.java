package com.oriente.landing.util;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lleva cualquier link de Instagram a su forma canonica. Sin estado.
 *
 * Instagram entrega el mismo contenido bajo varias formas segun desde donde se
 * copie: desde la web sale /p/CODIGO/, desde un perfil en la app sale
 * /usuario/p/CODIGO/, y el boton Compartir da /share/ALGO, que es un redirector.
 * Todas apuntan al mismo lugar, asi que se guardan iguales: de lo contrario la
 * misma publicacion entra dos veces y el chequeo de repetidos no sirve de nada.
 */
public final class NormalizadorDeUrlDeInstagram {

    /**
     * Acepta las formas que Instagram genera hoy:
     *   instagram.com/p|reel|reels|tv/CODIGO
     *   instagram.com/usuario/p|reel|reels|tv/CODIGO
     * con o sin www, con http o https, e instagr.am como alias del dominio.
     */
    private static final Pattern PUBLICACION = Pattern.compile(
            "^https?://(?:www\\.)?(?:instagram\\.com|instagr\\.am)/"
                    + "(?:[A-Za-z0-9_.]+/)?"
                    + "(p|reel|reels|tv)/([A-Za-z0-9_-]+)/?.*$",
            Pattern.CASE_INSENSITIVE);

    /** El link del boton Compartir: no dice que publicacion es, hay que seguirlo. */
    private static final Pattern PARA_COMPARTIR = Pattern.compile(
            "^https?://(?:www\\.)?(?:instagram\\.com|instagr\\.am)/share/(?:[A-Za-z0-9_-]+/)?[A-Za-z0-9_-]+/?.*$",
            Pattern.CASE_INSENSITIVE);

    /**
     * Lo que valida el request antes de llegar al servicio. Es mas laxo que
     * PUBLICACION porque tambien deja pasar los enlaces para compartir.
     */
    public static final String PATRON_DE_VALIDACION =
            "^https?://(www\\.)?(instagram\\.com|instagr\\.am)/"
                    + "((([A-Za-z0-9_.]+/)?(p|reel|reels|tv)/[A-Za-z0-9_-]+)"
                    + "|(share/([A-Za-z0-9_-]+/)?[A-Za-z0-9_-]+))/?.*$";

    public static final String MENSAJE_DE_VALIDACION =
            "Tiene que ser un link de Instagram: una publicacion, un reel o un enlace para compartir";

    private NormalizadorDeUrlDeInstagram() {
    }

    /**
     * Devuelve la URL canonica, o vacio si el link no es una publicacion
     * reconocible. Un /share/ sin resolver devuelve vacio: todavia no se sabe a
     * que publicacion apunta.
     */
    public static Optional<String> normalizar(String url) {
        if (url == null || url.isBlank()) {
            return Optional.empty();
        }

        Matcher publicacion = PUBLICACION.matcher(url.trim());
        if (!publicacion.matches()) {
            return Optional.empty();
        }

        String tipo = publicacion.group(1).toLowerCase(Locale.ROOT);
        String codigo = publicacion.group(2);

        // "reels" en plural aparece en algunos links y lleva al mismo lugar que
        // "reel": se guarda en una sola forma para no duplicar la publicacion.
        if ("reels".equals(tipo)) {
            tipo = "reel";
        }

        return Optional.of("https://www.instagram.com/" + tipo + "/" + codigo + "/");
    }

    /**
     * El codigo de la publicacion (lo que va despues de /p/ o /reel/). Es lo unico
     * que identifica el contenido: /p/CODIGO y /reel/CODIGO son la misma
     * publicacion.
     */
    public static Optional<String> codigo(String url) {
        if (url == null || url.isBlank()) {
            return Optional.empty();
        }
        Matcher publicacion = PUBLICACION.matcher(url.trim());
        return publicacion.matches() ? Optional.of(publicacion.group(2)) : Optional.empty();
    }

    public static boolean esEnlaceParaCompartir(String url) {
        return url != null && PARA_COMPARTIR.matcher(url.trim()).matches();
    }

    /** Reconoce cualquier link de Instagram que valga la pena intentar guardar. */
    public static boolean pareceDeInstagram(String url) {
        return normalizar(url).isPresent() || esEnlaceParaCompartir(url);
    }

    /** El embed muestra los reels distinto que las publicaciones comunes. */
    public static boolean esReel(String url) {
        return url != null && url.toLowerCase(Locale.ROOT).matches(".*/(reel|reels|tv)/.*");
    }
}
