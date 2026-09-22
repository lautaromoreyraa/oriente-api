package com.oriente.landing.domain;

import com.oriente.landing.enumeration.TipoDePublicacion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Un reel o un posteo de Instagram que ilustra un servicio en su tarjeta.
 *
 * El video y la imagen son copias en Cloudinary: las URLs del CDN de Instagram
 * vencen a las pocas horas. Del post se guarda el link, para que la tarjeta
 * lleve a la publicacion.
 */
@Entity
@Table(name = "publicacion_de_instagram",
       uniqueConstraints = @UniqueConstraint(name = "uk_publicacion_de_instagram_url", columnNames = "url"))
public class PublicacionDeInstagram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Column(nullable = false, unique = true, length = 500)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoDePublicacion tipo = TipoDePublicacion.POST;

    private String titulo;

    @Column(name = "miniatura_url", length = 500)
    private String miniaturaUrl;

    @Column(name = "miniatura_public_id")
    private String miniaturaPublicId;

    /**
     * El video de la publicacion, subido al mismo almacen que las imagenes.
     *
     * No se toma de Instagram: esas URLs vienen firmadas y expiran, igual que las
     * de las fotos. La miniatura hace de cuadro inicial mientras el video carga.
     */
    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "video_public_id")
    private String videoPublicId;

    @Column(nullable = false)
    private Integer orden = 0;

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

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TipoDePublicacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoDePublicacion tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMiniaturaUrl() {
        return miniaturaUrl;
    }

    public void setMiniaturaUrl(String miniaturaUrl) {
        this.miniaturaUrl = miniaturaUrl;
    }

    public String getMiniaturaPublicId() {
        return miniaturaPublicId;
    }

    public void setMiniaturaPublicId(String miniaturaPublicId) {
        this.miniaturaPublicId = miniaturaPublicId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoPublicId() {
        return videoPublicId;
    }

    public void setVideoPublicId(String videoPublicId) {
        this.videoPublicId = videoPublicId;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
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
