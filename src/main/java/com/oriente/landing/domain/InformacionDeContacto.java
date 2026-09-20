package com.oriente.landing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Telefono, redes y ubicacion.
 *
 * Estos datos estaban escritos en el frontend (constants/data.ts): cambiar un
 * numero de telefono requeria un deploy. Ahora se editan desde el panel.
 */
@Entity
@Table(name = "informacion_de_contacto")
public class InformacionDeContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // En formato internacional, tal como lo espera el link de wa.me.
    @Column(nullable = false, length = 30)
    private String whatsapp;

    @Column(name = "whatsapp_para_mostrar", length = 40)
    private String whatsappParaMostrar;

    @Column(length = 60)
    private String instagram;

    private String direccion;

    @Column(name = "url_de_maps", length = 500)
    private String urlDeMaps;

    @Column(name = "embed_de_maps", columnDefinition = "TEXT")
    private String embedDeMaps;

    @CreationTimestamp
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getWhatsappParaMostrar() {
        return whatsappParaMostrar;
    }

    public void setWhatsappParaMostrar(String whatsappParaMostrar) {
        this.whatsappParaMostrar = whatsappParaMostrar;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUrlDeMaps() {
        return urlDeMaps;
    }

    public void setUrlDeMaps(String urlDeMaps) {
        this.urlDeMaps = urlDeMaps;
    }

    public String getEmbedDeMaps() {
        return embedDeMaps;
    }

    public void setEmbedDeMaps(String embedDeMaps) {
        this.embedDeMaps = embedDeMaps;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }
}
