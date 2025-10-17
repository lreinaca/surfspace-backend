package com.eam.surfspace.domain.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear un nuevo usuario")
public class UserCreateDTO {
    @Schema(description = "Nombre completo",
            example = "Juan Velasco",
            required = true,
            maxLength = 100)
    private String nombre;

    @Schema(description = "Correo electrónico",
            example = "juan.v7z@gmail.com",
            required = true,
            maxLength = 150)
    private String email;

    @Schema(description = "Contraseña",
            example = "MiClaveSegura123",
            required = true,
            minLength = 8,
            maxLength = 60)
    private String contrasena;

    @Schema(description = "Teléfono",
            example = "3001234567",
            required = true,
            maxLength = 20)
    private String telefono;

    @Schema(description = "Rol",
            example = "AFILIADO",
            required = true,
            maxLength = 50)
    private String rol;
}
