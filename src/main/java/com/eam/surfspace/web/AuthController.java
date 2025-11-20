package com.eam.surfspace.web;

import com.eam.surfspace.domain.dto.JwtResponseDTO;
import com.eam.surfspace.domain.dto.LoginRequestDTO;
import com.eam.surfspace.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API para autenticación y gestión de tokens JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario con email y contraseña y devuelve un token JWT válido por 24 horas."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login exitoso - Token JWT generado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = JwtResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales incorrectas",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Solicitud mal formada - Verifica que el Content-Type sea application/json",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            // Log para depuración
            System.out.println("🔐 Intento de login para: " + loginRequest.getEmail());

            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getContrasena()
                )
            );

            // Logs de depuración
            System.out.println("Usuario autenticado: " + authentication.getName());
            System.out.println("Roles: " + authentication.getAuthorities());

            // Cargar los detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Generar el token JWT
            String jwt = jwtService.generateToken(userDetails);
            System.out.println("Token JWT generado exitosamente");

            // Devolver la respuesta con el token
            JwtResponseDTO response = new JwtResponseDTO(jwt, loginRequest.getEmail());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            System.out.println("Credenciales incorrectas para: " + loginRequest.getEmail());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciales incorrectas");
            error.put("message", "El email o la contraseña son incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            
        } catch (Exception e) {
            System.out.println("wqError durante el login: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/validate")
    @Operation(
        summary = "Validar token JWT",
        description = "Verifica si un token JWT es válido. Requiere el header Authorization: Bearer {token}"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token válido",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Token no proporcionado");
                error.put("message", "Debes incluir el header: Authorization: Bearer {token}");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("email", username);
                response.put("expiresAt", jwtService.extractExpiration(token));
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("valid", "false");
                error.put("message", "Token inválido o expirado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token inválido");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}

