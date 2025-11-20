package com.eam.surfspace.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para solicitud de login")
public class LoginRequestDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Schema(description = "Email del usuario", example = "alice@example.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario", example = "MiPassSegura123")
    private String contrasena;
}

