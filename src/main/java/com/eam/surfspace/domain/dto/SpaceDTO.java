package com.eam.surfspace.domain.dto;

import com.eam.surfspace.persistence.entity.EnumSpaceStatus;
import com.eam.surfspace.persistence.entity.EnumSpaceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceDTO {

    private Integer idSpace;
    private String name;
    private int capacity;
    private String description;
    private EnumSpaceType type;
    private EnumSpaceStatus status;

}