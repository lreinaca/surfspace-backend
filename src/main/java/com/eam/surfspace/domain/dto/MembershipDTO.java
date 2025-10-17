package com.eam.surfspace.domain.dto;

import com.eam.surfspace.persistence.entity.MembershipStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de la membresía (respuesta)")
public class MembershipDTO {
    @Schema(description = "ID de la membresía", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idMembresia;

    @Schema(description = "ID del usuario", example = "10", required = true)
    private Integer idUsuario;

    @Schema(description = "Nombre del usuario", example = "Juan Diego", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombre;

    @Schema(description = "Fecha de inicio (YYYY-MM-DD)", example = "2025-10-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin (YYYY-MM-DD)", example = "2026-10-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;

    @Schema(description = "Estado de la membresía", example = "ACTIVA")
    private MembershipStatus estado;
}
