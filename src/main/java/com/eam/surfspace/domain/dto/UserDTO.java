package com.eam.surfspace.domain.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de usuario (respuesta)")
public class UserDTO {

    @Schema(description = "ID del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Schema(description = "Nombre", example = "Juan Diego", required = true)
    private String nombre;

    @Schema(description = "Correo electrónico", example = "juan.d7@email.com", required = true)
    private String email;

    @Schema(description = "Número de teléfono", example = "3103768461", required = true)
    private String telefono;

    @Schema(description = "Rol", example = "AFILIADO", required = true)
    private String rol;
}
