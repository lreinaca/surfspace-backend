package com.eam.surfspace.web;

import com.eam.surfspace.domain.service.SpaceService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import com.eam.surfspace.persistence.entity.SpaceEntity;


@RestController
@RequestMapping("/api/espacios")
@Tag(name = "Spaces", description = "API for managing spaces in the system")
public class SpaceController {
    private final SpaceService spaceService;

    @Autowired
    public SpaceController(SpaceService spaceService){
        this.spaceService = spaceService;
    }

    //CREATE
    @PostMapping 
    @Operation(summary = "Create a new space", description = "Creates a new space in the system")
    @ApiResponses(value= {
        @ApiResponse(responseCode = "201", description = "Space created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SpaceEntity> createSpace(@RequestBody SpaceEntity space){
        //Códigooo
        return null;
    }

    //READ ALL
    @GetMapping
    @Operation(summary = "Get all spaces", description = "Retrieves all spaces in the system")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "Spaces retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SpaceEntity>> getAllSpaces(){
        //Códigooo
        return null;
    }

    //READ BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Get space by ID", description = "Retrieves a space by identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Space retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Space not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SpaceEntity> getSpaceById(@PathVariable @Parameter(description = "Space identifier", required = true)int id){
        //Códigoooo
        return null;
    }

    //UPDATE
    @PutMapping("/{id}")
    @Operation(summary = "Update space", description = "Updates an existing space in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Space updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Space not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SpaceEntity> updateSpace(@PathVariable @Parameter(description = "Space identifier", required = true)int id,
                                                   @RequestBody @Parameter(description = "Updated space data", required = true)SpaceEntity space){
        //Códigoooo
        return null;
    }

    //DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate space", description = "Deactivates a space from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Space deactivate successfully"),
        @ApiResponse(responseCode = "404", description = "Space not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteSpace(@PathVariable @Parameter(description = "Space identifier", required = true)int id){
        //Códigooooo
        return null;
    }

}
