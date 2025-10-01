package com.eam.surfspace.web;


import com.eam.surfspace.domain.dto.UserCreateDTO;
import com.eam.surfspace.domain.dto.UserDTO;
import com.eam.surfspace.domain.dto.UserUpdateDTO;
import com.eam.surfspace.domain.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eam.surfspace.persistence.entity.UserEntity;
import com.eam.surfspace.persistence.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API for users management")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Crear un nuevo usuario
    @PostMapping
    @Operation(summary = "Crear un nuevo usuario",
            description = "Crea un nuevo usuario con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados")
    })
    public ResponseEntity<UserDTO> createUser(
            @RequestBody @Parameter(description = "Datos del usuario a crear") UserCreateDTO user) {
        try {
            UserDTO saved = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ✅ Obtener todos los usuarios
    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito")})
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // ✅ Obtener usuario por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario específico basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable @Parameter(description = "ID del usuario") Integer id) {
        Optional<UserDTO> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Actualizar usuario
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario", description = "Actualiza los datos de un usuario existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Integer id,
            @RequestBody UserUpdateDTO updatedUser) {
        Optional<UserDTO> updated = userService.update(id, updatedUser);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Eliminar usuario
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        boolean deleted = userService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/login")
    @Operation(summary = "Login de usuario", description = "Valida credenciales del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    public ResponseEntity<String> loginUser(
            @RequestBody @Parameter(description = "Credenciales del usuario") UserCreateDTO loginRequest) {
        // Aquí deberías comparar email + contraseña con la DB (en UserDAO o en un AuthService)
        // Por ahora solo simulo que siempre responde 200 si no es vacío
        if (loginRequest.getEmail() != null && loginRequest.getContrasena() != null) {
            return ResponseEntity.ok("Login exitoso");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
    }
}
