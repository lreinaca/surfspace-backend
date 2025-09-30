package com.eam.surfspace.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear nueva membresía")
public class MembershipCreateDTO {
    @Schema(description = "ID del usuario de la membresía",
            example = "10",
            required = true)
    private Integer userId;

    @Schema(description = "Fecha de inicio (YYYY-MM-DD)",
            example = "2025-10-01",
            required = true)
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de finalización (YYYY-MM-DD)",
            example = "2026-10-01",
            required = true)
    private LocalDate fechaFin;

    @Schema(description = "Estado",
            example = "ACTIVA",
            required = true,
            maxLength = 20)
    private String estado;
}

