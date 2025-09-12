package com.eam.surfspace.web;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eam.surfspace.persistence.entity.UserEntity;
import com.eam.surfspace.persistence.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
@Tag(name = "usuario", description = "API para la gestión de usuarios")
public class UserController {

    private UserRepository userRepository;
    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    @Operation(summary = "Loguear Usuario",
            description = "Valida las credenciales del usuario para el inicio de sesión.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario logueado con éxito"),
            @ApiResponse(responseCode = "401", description = "Usuario o contraseña incorrectos")})
    public ResponseEntity<UserEntity> loginUser(@RequestBody @Parameter(description = "Credenciales de Acceso") UserEntity user) { //utilizar un dto
        return null;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario",
            description = "Crea un nuevo usuario con los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con éxito"),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe")})
    public ResponseEntity<UserEntity> createUser(
            @RequestBody @Parameter(description = "Datos del usuario a crear") UserEntity user ) {
        return null;
    }


    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")})
    public ResponseEntity<UserEntity> getAllUsers(@RequestHeader(value = "Authorization", required = false) @Parameter(description = "Token de autorización JWT", name = "Authorization", example = "Bearer <token>") String authHeader) {
        return null;
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID",
            description = "Devuelve un usuario específico basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    public ResponseEntity<UserEntity> getUserById(@PathVariable @Parameter(description = "ID del usuario") Integer id,
                                            @RequestHeader(value = "Authorization", required = false) @Parameter(description = "Token de autorización JWT" , name = "Authorization", example = "Bearer <token>") String authHeader) {
        return null;
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario", description = "Actualiza los datos de un usuario existente.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito"), @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    public ResponseEntity<UserEntity> updateUsuario(@PathVariable @Parameter(description = "ID del usuario") Integer id, @RequestBody @Parameter(description = "Datos actualizados del usuario") UserEntity user, @RequestHeader(value = "Authorization", required = false) @Parameter(description = "Token de autorización JWT", name = "Authorization", example = "Bearer <token>") String authHeader) {
        return null;
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario basado en su ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Usuario eliminado con éxito"), @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    public ResponseEntity<UserEntity> deleteUser(@PathVariable @Parameter(description = "ID del usuario") Integer id, @RequestHeader(value = "Authorization", required = false) @Parameter(description = "Token de autorización JWT",name = "Authorization", example = "Bearer <token>") String authHeader) {
        return null;
    }
}
