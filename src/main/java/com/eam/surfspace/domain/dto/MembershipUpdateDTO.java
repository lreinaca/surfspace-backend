package com.eam.surfspace.domain.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar una membresía")
public class MembershipUpdateDTO {

    @Schema(description = "Estado actual de la membresía",
            example = "ACTIVA")
    private String estado;

    @Schema(description = "Nueva fecha de inicio de la membresía",
            example = "2025-10-01")
    private LocalDate fechaInicio;

    @Schema(description = "Nueva fecha de finalización de la membresía",
            example = "2026-01-01")
    private LocalDate fechaFin;
}
