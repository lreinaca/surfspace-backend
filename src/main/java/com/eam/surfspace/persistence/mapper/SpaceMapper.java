package com.eam.surfspace.persistence.mapper;

import com.eam.surfspace.domain.dto.SpaceDTO;
import com.eam.surfspace.persistence.entity.EnumSpaceStatus;
import com.eam.surfspace.persistence.entity.EnumSpaceType;
import com.eam.surfspace.persistence.entity.SpaceEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface SpaceMapper {

    //Entity -> DTO
    @Mapping(source = "status", target = "status", qualifiedByName = "enumStatusToString")
    @Mapping(source= "type", target= "type", qualifiedByName = "enumTypeToString")
    SpaceDTO toSpaceDTO(SpaceEntity spaceEntity);

    //DTO -> Entity
    @Mapping(source = "status", target = "status", qualifiedByName = "stringToEnumStatus")
    @Mapping(source= "type", target= "type", qualifiedByName = "stringToEnumType")
    SpaceEntity toSpaceEntity(SpaceDTO spaceDTO);

    List<SpaceDTO> toSpaceDTOList(List<SpaceEntity> spaceEntities);

    List<SpaceEntity> toSpaceEntityList(List<SpaceDTO> spaceDTOs);

    // Enum 'Status' <-> String ------------------------------------------------------
    @Named("enumStatusToString")
    static String enumStatusToString(EnumSpaceStatus enumStatus) {
        return enumStatus != null ? enumStatus.name() : null;
    }

    @Named("stringToEnumStatus")
    static EnumSpaceStatus stringToEnumStatus(String status) {
        return status != null ? EnumSpaceStatus.valueOf(status.toUpperCase()) : null;
    }

    @Mapping(target = "idSpace", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(SpaceDTO spaceDTO, @MappingTarget SpaceEntity entity);

    // Enum 'Type' <-> String ------------------------------------------------------
    @Named("enumTypeToString")
    static String enumTypeToString(EnumSpaceType enumType) {
        return enumType != null ? enumType.name() : null;
    }

    @Named("stringToEnumType")
    static EnumSpaceType stringToEnumType(String status) {
        return status != null ? EnumSpaceType.valueOf(status.toUpperCase()) : null;
    }

}

