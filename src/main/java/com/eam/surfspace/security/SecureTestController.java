package com.eam.surfspace.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/secure")
@Tag(name = "Security Test", description = "Endpoints para probar autenticación JWT")
public class SecureTestController {

    @GetMapping("/me")
    @Operation(
            summary = "Obtener información del usuario autenticado",
            description = "Endpoint protegido que devuelve el email y roles del usuario autenticado mediante JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Usuario autenticado correctamente")
    @ApiResponse(responseCode = "401", description = "No autenticado o token inválido")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("email", auth.getName());
        response.put("authorities", auth.getAuthorities());
        response.put("authenticated", true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    @Operation(
            summary = "Test de autenticación simple",
            description = "Endpoint protegido simple para verificar que el JWT funciona",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Token válido")
    @ApiResponse(responseCode = "401", description = "Token inválido o ausente")
    public ResponseEntity<String> testAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("¡JWT funciona! Usuario: " + auth.getName());
    }
}

