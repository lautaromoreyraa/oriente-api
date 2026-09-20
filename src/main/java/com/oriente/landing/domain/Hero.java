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

@Entity
@Table(name = "hero")
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String subtitulo;

    @Column(name = "texto_del_cta")
    private String textoDelCta;

    @Column(name = "url_del_cta")
    private String urlDelCta;

    @Column(name = "imagen_de_fondo_url", length = 500)
    private String imagenDeFondoUrl;

    // El public_id de Cloudinary se guarda para poder borrar el archivo cuando la
    // imagen se reemplaza. Sin esto la cuenta se llena de huerfanos.
    @Column(name = "imagen_de_fondo_public_id")
    private String imagenDeFondoPublicId;

    @Column(name = "imagen_de_fondo_alt")
    private String imagenDeFondoAlt;

    @Column(nullable = false)
    private Boolean activo = true;

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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getTextoDelCta() {
        return textoDelCta;
    }

    public void setTextoDelCta(String textoDelCta) {
        this.textoDelCta = textoDelCta;
    }

    public String getUrlDelCta() {
        return urlDelCta;
    }

    public void setUrlDelCta(String urlDelCta) {
        this.urlDelCta = urlDelCta;
    }

    public String getImagenDeFondoUrl() {
        return imagenDeFondoUrl;
    }

    public void setImagenDeFondoUrl(String imagenDeFondoUrl) {
        this.imagenDeFondoUrl = imagenDeFondoUrl;
    }

    public String getImagenDeFondoPublicId() {
        return imagenDeFondoPublicId;
    }

    public void setImagenDeFondoPublicId(String imagenDeFondoPublicId) {
        this.imagenDeFondoPublicId = imagenDeFondoPublicId;
    }

    public String getImagenDeFondoAlt() {
        return imagenDeFondoAlt;
    }

    public void setImagenDeFondoAlt(String imagenDeFondoAlt) {
        this.imagenDeFondoAlt = imagenDeFondoAlt;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }
}
