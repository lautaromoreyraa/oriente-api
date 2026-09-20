package com.oriente.landing.service.administracion.imagen;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface BorradorDeImagenes {

    /**
     * Borra los archivos en Cloudinary. Nunca lanza: un fallo del proveedor no
     * puede deshacer un borrado que en la base ya ocurrio.
     */
    void borrar(Set<String> publicIds);

    /** Los public_id que estaban antes y ya no estan despues. */
    static Set<String> loQueSobra(Collection<String> antes, Collection<String> despues) {
        Set<String> huerfanas = new HashSet<>(antes);
        huerfanas.removeAll(despues);
        huerfanas.removeIf(publicId -> publicId == null || publicId.isBlank());
        return huerfanas;
    }
}
