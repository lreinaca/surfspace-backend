package com.eam.surfspace.persistence.mapper;

import com.eam.surfspace.domain.dto.MembershipCreateDTO;
import com.eam.surfspace.domain.dto.MembershipDTO;
import com.eam.surfspace.domain.dto.MembershipUpdateDTO;
import com.eam.surfspace.persistence.entity.MembershipEntity;
import com.eam.surfspace.persistence.entity.MembershipStatus;
import com.eam.surfspace.persistence.entity.UserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface MembershipMapper {

    @Mapping(target = "idMembresia", source = "idMembresia")
    @Mapping(target = "idUsuario", source = "idUsuario.idUsuario")
    @Mapping(target = "nombre", source = "idUsuario.nombre")
    MembershipDTO toDTO(MembershipEntity entity);

    List<MembershipDTO> toDTOList(List<MembershipEntity> entities);

    @Mapping(target = "idMembresia", ignore = true)
    @Mapping(target = "idUsuario", source = "idUsuario", qualifiedByName = "createUserEntityFromId")
    MembershipEntity toEntity(MembershipCreateDTO createDTO);

    @Named("createUserEntityFromId")
    default UserEntity createUserEntityFromId(Integer userId) {
        if (userId == null) return null;
        UserEntity user = new UserEntity();
        user.setIdUsuario(userId);
        return user;
    }
    void updateEntityFromDTO(MembershipUpdateDTO updateDTO, @MappingTarget MembershipEntity entity);
}