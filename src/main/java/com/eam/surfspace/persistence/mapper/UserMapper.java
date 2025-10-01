package com.eam.surfspace.persistence.mapper;

import com.eam.surfspace.domain.dto.UserCreateDTO;
import com.eam.surfspace.domain.dto.UserDTO;
import com.eam.surfspace.domain.dto.UserUpdateDTO;
import com.eam.surfspace.persistence.entity.UserEntity;
import org.mapstruct.*;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UserMapper {


    // Convierte lista de entidades → lista de DTOs
    List<UserDTO> toDTOList(List<UserEntity> entities);

    // Convierte DTO de creación → entidad
    @Mapping(target = "idUsuario", ignore = true)
    // se genera en DB
    UserEntity toEntity(UserCreateDTO createDTO);

    // Actualiza una entidad existente con los datos de un DTO de actualización
    void updateEntityFromDTO(UserUpdateDTO updateDTO, @MappingTarget UserEntity entity);


    UserDTO toDTO(UserEntity entity);
}





