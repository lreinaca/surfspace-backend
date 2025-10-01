package com.eam.surfspace.persistence.mapper;
import com.eam.surfspace.domain.dto.UserCreateDTO;
import com.eam.surfspace.domain.dto.UserDTO;
import com.eam.surfspace.persistence.entity.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)

public interface UserMapper {
//
//    @Mapping(target = "contrasena", ignore = true) // no muestra la contraseña
//    UserDTO toDTO(UserEntity entity);
//
//    List<UserDTO> toDTOList(List<UserEntity> entities);
//
//    @Mapping(target = "idUsuario", ignore = true)
//    UserEntity toEntity(UserCreateDTO createDTO);
//
//    @Mapping(target = "idUsuario", ignore = true)
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateEntityFromDTO(UserDTO dto, @MappingTarget UserEntity entity);
}
