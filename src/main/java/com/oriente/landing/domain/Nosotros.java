package com.oriente.landing.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * La seccion "nosotros" de la landing.
 *
 * Las estadisticas, los diferenciales y el equipo eran columnas numeradas
 * (stat1, stat2, diff1..diff4) y un JSON de URLs. Ahora son listas: se agregan,
 * se reordenan y se desactivan sin tocar el esquema.
 */
@Entity
@Table(name = "nosotros")
public class Nosotros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String cuerpo;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "imagen_public_id")
    private String imagenPublicId;

    @Column(name = "imagen_alt")
    private String imagenAlt;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "nosotros", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<Estadistica> estadisticas = new ArrayList<>();

    @OneToMany(mappedBy = "nosotros", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<Diferencial> diferenciales = new ArrayList<>();

    @OneToMany(mappedBy = "nosotros", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<MiembroDelEquipo> equipo = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    public void agregarEstadistica(Estadistica estadistica) {
        estadistica.setNosotros(this);
        this.estadisticas.add(estadistica);
    }

    public void vaciarEstadisticas() {
        this.estadisticas.forEach(estadistica -> estadistica.setNosotros(null));
        this.estadisticas.clear();
    }

    public void agregarDiferencial(Diferencial diferencial) {
        diferencial.setNosotros(this);
        this.diferenciales.add(diferencial);
    }

    public void vaciarDiferenciales() {
        this.diferenciales.forEach(diferencial -> diferencial.setNosotros(null));
        this.diferenciales.clear();
    }

    public void agregarMiembro(MiembroDelEquipo miembro) {
        miembro.setNosotros(this);
        this.equipo.add(miembro);
    }

    public void vaciarEquipo() {
        this.equipo.forEach(miembro -> miembro.setNosotros(null));
        this.equipo.clear();
    }

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

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getImagenPublicId() {
        return imagenPublicId;
    }

    public void setImagenPublicId(String imagenPublicId) {
        this.imagenPublicId = imagenPublicId;
    }

    public String getImagenAlt() {
        return imagenAlt;
    }

    public void setImagenAlt(String imagenAlt) {
        this.imagenAlt = imagenAlt;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<Estadistica> getEstadisticas() {
        return estadisticas;
    }

    public List<Diferencial> getDiferenciales() {
        return diferenciales;
    }

    public List<MiembroDelEquipo> getEquipo() {
        return equipo;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }
}
