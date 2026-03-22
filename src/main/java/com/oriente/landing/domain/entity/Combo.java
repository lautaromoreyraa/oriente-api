package com.oriente.landing.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "combo", uniqueConstraints = @UniqueConstraint(columnNames = "slug"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column
    private String tagline;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String badge;

    @Column(name = "display_order", columnDefinition = "INT DEFAULT 0")
    private Integer displayOrder = 0;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean active = true;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "combo")
    @Builder.Default
    private List<ComboItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
