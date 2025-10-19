package com.eam.surfspace.persistence.mapper;

import com.eam.surfspace.domain.dto.NotificationDTO;
import com.eam.surfspace.persistence.entity.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface NotificationMapper {
    // Define los métodos de mapeo entre NotificationEntity y NotificationDTO
    @Mapping(source = "idNotification", target = "notificationId")
    @Mapping(source = "booking.bookingId", target = "bookingId")
    @Mapping(source = "sentDate", target = "sentAt")
    @Mapping(target = "confirmationCode", ignore = true)
    NotificationDTO toDto(NotificationEntity entity);

    @Mapping(source = "notificationId", target = "idNotification")
    @Mapping(source = "bookingId", target = "booking.bookingId")
    @Mapping(source = "sentAt", target = "sentDate")
    // Ignorar relaciones completas si no se quieren mapear desde el DTO
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "pago", ignore = true)
    NotificationEntity toEntity(NotificationDTO dto);

    List<NotificationDTO> toDtoList(List<NotificationEntity> entities);

}
