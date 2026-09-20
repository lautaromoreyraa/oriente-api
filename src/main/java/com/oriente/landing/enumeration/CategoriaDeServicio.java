package com.oriente.landing.enumeration;

/**
 * Las dos disciplinas del consultorio.
 *
 * Los nombres quedan como estaban (KINE, ESTETICA) y no se traducen: son los
 * valores ya persistidos en la base y los que el frontend compara para separar
 * los servicios en bloques. Renombrarlos obligaria a una migracion de datos que
 * no compra nada.
 */
public enum CategoriaDeServicio {
    KINE,
    ESTETICA
}
