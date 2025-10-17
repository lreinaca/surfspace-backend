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
    List<UserDTO> toDTOList(List<UserEntity> entities);

    @Mapping(source = "idUsuario", target = "id")
    UserDTO toDTO(UserEntity entity);

    @Mapping(target = "idUsuario", ignore = true)
    UserEntity toEntity(UserCreateDTO createDTO);

    void updateEntityFromDTO(UserUpdateDTO updateDTO, @MappingTarget UserEntity entity);
}




