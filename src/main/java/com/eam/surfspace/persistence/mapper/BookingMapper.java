package com.eam.surfspace.persistence.mapper;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.persistence.entity.BookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.sql.Time;
import java.util.Date;

// convierte de un tipo de elmento a otro, es mapear de entidades a dtos
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface BookingMapper {

   // @Mapping(target = "idBookig" , source = "idBookig"); como tienen el mismo nombre no se necesitan si en la entity se llamara distinto se usaria

    /**
     *  Este es el metodo toBookingDto para mapear de una entidad a un dto
     * @param booking
     * @return
     */
    BookingResponseDTO toBookingDto(BookingEntity booking); // este es un método que no esta implementado por eso no tiene cuerpo

    /**
     * Este método conviente un DTO en una entidad
     * @param bookingRequestDTO
     * @return
     */
    BookingEntity toBooking(BookingRequestDTO bookingRequestDTO);




}
