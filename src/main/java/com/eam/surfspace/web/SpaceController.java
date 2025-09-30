package com.eam.surfspace.web;

import com.eam.surfspace.domain.dto.SpaceDTO;
import com.eam.surfspace.domain.service.SpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import com.eam.surfspace.persistence.entity.SpaceEntity;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/spaces")
@Tag(name = "Spaces", description = "API for managing spaces in the system")
public class SpaceController {

    private final SpaceService spaceService;

    //CREATE SPACE -----------------------------------------------------------------
    @PostMapping
    @Operation(summary = "Create a new space", description = "Creates a new space in the system")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "201", description = "Space created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SpaceDTO> createSpace(
            @Parameter(description = "datos del espacio a crear", required = true)
            @RequestBody SpaceDTO spaceDTO){
        try {
            SpaceDTO createdSpace = spaceService.createSpace(spaceDTO);
            log.info("space created successfully\n");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSpace);
        } catch (RuntimeException e) {
            log.warn("Could not create space: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    //READ ALL
    @GetMapping
    @Operation(summary = "Get all spaces", description = "Retrieves all spaces in the system")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Spaces retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SpaceDTO>> getAllSpaces(){
        log.debug("GET /api/spaces - Retrieving all spaces");

        try {
            List<SpaceDTO> spaces = spaceService.getAllSpaces();
            log.debug("{} spaces were found", spaces.size());
            return ResponseEntity.ok(spaces);
        } catch (RuntimeException e) {
            log.warn("Could not retrieve spaces: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    //READ BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Get space by ID", description = "Retrieves a space by identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Space retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Space not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SpaceDTO> getSpaceById(
            @Parameter(description = "Space identifier", required = true)
            @PathVariable int id
    ){
        log.debug("GET /api/spaces/{} - Retrieving space by ID", id);

        try {
            SpaceDTO space = spaceService.getSpaceById(id);
            return ResponseEntity.ok(space);
        } catch (RuntimeException e) {
            log.warn("Could not retrieve space: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    //UPDATE
    @PutMapping("/{idSpace}")
    @Operation(summary = "Update space", description = "Updates an existing space in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Space updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Space not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SpaceDTO> updateSpace(
            @Parameter(description = "space id to update", required = true)
            @PathVariable int idSpace,
            @Parameter(description = "Updated space data", required = true)
            @RequestBody SpaceDTO updateDTO
    ){
        log.debug("PUT /api/spaces/{} - Updating space", idSpace);

        try {
            SpaceDTO updatedSpace = spaceService.updateSpace(idSpace, updateDTO);
            log.info("space updated successfully");
            return ResponseEntity.ok(updatedSpace);
        } catch (RuntimeException e) {
            log.warn("Could not update space: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    //DEACTIVATE
    @PatchMapping("/{id}")
    @Operation(summary = "Deactivate space", description = "Deactivates a space from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Space deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Space not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deactivateSpace(
            @Parameter(description = "Space identifier", required = true)
            @PathVariable int id
    ){
        log.info("PATCH /api/spaces/{} - Deactivating space", id);

        try {
            spaceService.deactivateSpace(id);
            log.info("space deactivated successfully");
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Could not deactivate space: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}