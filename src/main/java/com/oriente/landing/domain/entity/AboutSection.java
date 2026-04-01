package com.oriente.landing.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "about_section")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AboutSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String heading;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean active = true;

    // ── Estadísticas editables ────────────────────────────
    @Column(name = "stat1_value", length = 20)
    private String stat1Value;

    @Column(name = "stat1_label", length = 60)
    private String stat1Label;

    @Column(name = "stat2_value", length = 20)
    private String stat2Value;

    @Column(name = "stat2_label", length = 60)
    private String stat2Label;

    @Column(name = "stat3_value", length = 20)
    private String stat3Value;

    @Column(name = "stat3_label", length = 60)
    private String stat3Label;

    // ── Diferenciales editables ───────────────────────────
    @Column(name = "diff1_title", length = 120)
    private String diff1Title;

    @Column(name = "diff1_desc", columnDefinition = "TEXT")
    private String diff1Desc;

    @Column(name = "diff2_title", length = 120)
    private String diff2Title;

    @Column(name = "diff2_desc", columnDefinition = "TEXT")
    private String diff2Desc;

    @Column(name = "diff3_title", length = 120)
    private String diff3Title;

    @Column(name = "diff3_desc", columnDefinition = "TEXT")
    private String diff3Desc;

    @Column(name = "diff4_title", length = 120)
    private String diff4Title;

    @Column(name = "diff4_desc", columnDefinition = "TEXT")
    private String diff4Desc;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}