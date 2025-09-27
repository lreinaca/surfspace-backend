package com.eam.surfspace.domain.dto;

import com.eam.surfspace.persistence.entity.EnumSpaceStatus;
import com.eam.surfspace.persistence.entity.EnumSpaceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceDTO {

    @Schema(description = "Unique ID of the space", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idSpace;

    @Schema(description = "Name of the space", example = "Sala de reuniones #123")
    private String name;

    @Schema(description = "Maximum capacity of the space", example = "20")
    private int capacity;

    @Schema(description = "Short description of the space", example = "Perfecta para reuniones ejecutivas y presentaciones.")
    private String description;

    @Schema(description = "Type of space")
    private EnumSpaceType type;

    @Schema(description = "Current status of the space")
    private EnumSpaceStatus status;

}