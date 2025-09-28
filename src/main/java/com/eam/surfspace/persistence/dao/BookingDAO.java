package com.eam.surfspace.persistence.dao;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.persistence.entity.BookingEntity;
import com.eam.surfspace.persistence.mapper.BookingMapper;
import com.eam.surfspace.persistence.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor

public class BookingDAO {
    // Lógica de acceso a datos personalizada
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    // Antes de guardar la reserva vamos a validar que no exista una reserva que se cruce con esos horarios en el mismo espacio
    public boolean existsBySpaceIdAndTimeOverlap(Integer spaceId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return bookingRepository.existsBySpaceIdAndTimeOverlap(spaceId, startDateTime, endDateTime);
    }
    // CREATE
    public BookingResponseDTO save(BookingRequestDTO bookingRequestDTO) {
        if (existsBySpaceIdAndTimeOverlap(bookingRequestDTO.getIdSpace(),
                bookingRequestDTO.getStartDateTime(), bookingRequestDTO.getEndDateTime())) {
            throw new IllegalArgumentException("El espacio no está disponible en el horario solicitado.");
        }
        BookingEntity bookingToSave = bookingMapper.toBooking(bookingRequestDTO);
        BookingEntity bookingSaved =  bookingRepository.save(bookingToSave);
        return bookingMapper.toBookingDto(bookingSaved);
    }


}
