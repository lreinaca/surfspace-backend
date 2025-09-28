package com.eam.surfspace.persistence.mapper;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.persistence.entity.BookingEntity;
import com.eam.surfspace.persistence.entity.EnumBookingStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;


// convierte de un tipo de elmento a otro, es mapear de entidades a dtos y visceversa
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)

public interface BookingMapper {

    // --- Entity -> ResponseDTO ---
    @Mapping(source = "status", target = "status", qualifiedByName = "enumToString")
    BookingResponseDTO toBookingDto(BookingEntity booking);

    // --- RequestDTO -> Entity ---
    @Mapping(source = "status", target = "status", qualifiedByName = "stringToEnum")
    @Mapping(target = "bookingId", ignore = true) // lo genera la BD
    @Mapping(target = "pago", ignore = true)      // se gestiona aparte
    BookingEntity toBooking(BookingRequestDTO bookingRequestDTO);

    // --- Converters Enum <-> String ---
    @Named("enumToString")
    static String enumToString(EnumBookingStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToEnum")
    static EnumBookingStatus stringToEnum(String status) {
        return status != null ? EnumBookingStatus.valueOf(status.toUpperCase()) : null;
    }

}

