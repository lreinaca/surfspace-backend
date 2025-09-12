package com.eam.surfspace.web;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eam.surfspace.persistence.entity.MembershipEntity;
import com.eam.surfspace.persistence.repository.MembershipRepository;

import java.util.List;

@RestController
@RequestMapping("/api/membership")
@Tag(name = "membresia", description = "API para la gestión de membresia")
public class MembershipController {
    private MembershipRepository membershipRepository;
    @Autowired
    public MembershipController(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva membresía", description = "Crea una nueva membresía para un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Membresía creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados")
    })
    public ResponseEntity<MembershipEntity> createMembership(
            @RequestBody @Parameter(description = "Datos de la membresía a crear") MembershipEntity membership) {
        return null;
    }


    @GetMapping
    @Operation(summary = "Obtener todas las membresías", description = "Devuelve una lista de todas las membresías registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de membresías obtenida con éxito")
    })
    public ResponseEntity<List<MembershipEntity>> getAllMemberships() {
        return null;
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener membresía por ID", description = "Devuelve una membresía específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membresía encontrada"),
            @ApiResponse(responseCode = "404", description = "Membresía no encontrada")
    })
    public ResponseEntity<MembershipEntity> getMembershipById(
            @PathVariable @Parameter(description = "ID de la membresía") Integer id) {
        return null;
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una membresía", description = "Actualiza los datos de una membresía existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membresía actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Membresía no encontrada")
    })
    public ResponseEntity<MembershipEntity> updateMembership(
            @PathVariable Integer id, @RequestBody MembershipEntity updatedMembership) {

        return null;
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una membresía", description = "Elimina una membresía por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membresía eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Membresía no encontrada")
    })
    public ResponseEntity<MembershipEntity> deleteMembership(@PathVariable Integer id) {
        return null;
    }
}
