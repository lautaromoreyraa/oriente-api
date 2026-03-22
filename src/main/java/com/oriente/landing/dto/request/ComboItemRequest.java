package com.oriente.landing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboItemRequest {

    //ComboItemRequest
    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    private Integer displayOrder = 0;
}
