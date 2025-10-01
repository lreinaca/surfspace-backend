package com.eam.surfspace.web;
import com.eam.surfspace.domain.dto.MembershipCreateDTO;
import com.eam.surfspace.domain.dto.MembershipDTO;
import com.eam.surfspace.domain.dto.MembershipUpdateDTO;
import com.eam.surfspace.domain.service.impl.MembershipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eam.surfspace.persistence.entity.MembershipEntity;
import com.eam.surfspace.persistence.repository.MembershipRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/memberships")
@Tag(name = "Memberships", description = "API for managing memberships")
public class MembershipController {
    private final MembershipService membershipService;

    @Autowired
    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }
    @PostMapping
    @Operation(summary = "Crear una nueva membresía", description = "Crea una nueva membresía para un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Membresía creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados")
    })
    public ResponseEntity<MembershipDTO> createMembership(
            @RequestBody @Parameter(description = "Datos de la membresía a crear") MembershipCreateDTO membership) {
        try {
            MembershipDTO saved = membershipService.save(membership);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping
    @Operation(summary = "Obtener todas las membresías", description = "Devuelve una lista de todas las membresías registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de membresías obtenida con éxito")
    })
    public ResponseEntity<List<MembershipDTO>> getAllMemberships() {
        List<MembershipDTO> memberships = membershipService.findAll();
        return ResponseEntity.ok(memberships);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener membresía por ID", description = "Devuelve una membresía específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membresía encontrada"),
            @ApiResponse(responseCode = "404", description = "Membresía no encontrada")
    })
    public ResponseEntity<MembershipDTO> getMembershipById(
            @PathVariable @Parameter(description = "ID de la membresía") Integer id) {
        Optional<MembershipDTO> membership = membershipService.findById(id);
        return membership.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una membresía", description = "Actualiza los datos de una membresía existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membresía actualizada con éxito"),
            @ApiResponse(responseCode = "404", description = "Membresía no encontrada")
    })
    public ResponseEntity<MembershipDTO> updateMembership(
            @PathVariable Integer id,
            @RequestBody MembershipUpdateDTO updatedMembership) {

        Optional<MembershipDTO> updated = membershipService.update(id, updatedMembership);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una membresía", description = "Elimina una membresía por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membresía eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Membresía no encontrada")
    })
    public ResponseEntity<Void> deleteMembership(@PathVariable Integer id) {
        boolean deleted = membershipService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
