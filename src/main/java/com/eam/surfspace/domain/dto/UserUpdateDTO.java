package com.eam.surfspace.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar un usuario existente")
public class UserUpdateDTO {

    @Schema(description = "ID del usuario a actualizar", example = "1", required = true)
    private Integer idUsuario;

    @Schema(description = "Nombre completo del usuario", example = "Ana María")
    private String nombre;

    @Schema(description = "Correo electrónico del usuario", example = "ana.maria@email.com")
    private String email;

    @Schema(description = "Contraseña del usuario (se almacenará encriptada)", example = "NuevaClaveSegura123", required = false)
    private String contrasena;

    @Schema(description = "Teléfono del usuario", example = "3109876543")
    private String telefono;

    @Schema(description = "Rol del usuario (ej: ADMIN, USUARIO, VISITANTE)", example = "USUARIO")
    private String rol;
}
