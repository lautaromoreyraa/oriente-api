package com.oriente.landing.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oriente.landing.domain.enums.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {

    private Long id;

    private String slug;

    private ServiceCategory category;

    private String title;

    private String description;

    private String iconPath;

    private Integer displayOrder;

    private Boolean active;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
