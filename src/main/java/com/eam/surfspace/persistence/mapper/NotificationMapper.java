package com.eam.surfspace.persistence.mapper;

import com.eam.surfspace.domain.dto.NotificationDTO;
import com.eam.surfspace.persistence.entity.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface NotificationMapper {
    // Define los métodos de mapeo entre NotificationEntity y NotificationDTO
    NotificationDTO toDto(NotificationEntity entity);
    NotificationEntity toEntity(NotificationDTO dto);
    List<NotificationDTO> toDtoList(List<NotificationEntity> entities);

}
