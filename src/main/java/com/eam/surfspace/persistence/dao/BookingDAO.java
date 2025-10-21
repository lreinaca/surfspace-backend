package com.eam.surfspace.persistence.dao;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.persistence.entity.BookingEntity;
import com.eam.surfspace.persistence.entity.EnumBookingStatus;
import com.eam.surfspace.persistence.mapper.BookingMapper;
import com.eam.surfspace.persistence.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        bookingToSave.setStatus(EnumBookingStatus.PENDIENTE); // Estado inicial
        BookingEntity bookingSaved =  bookingRepository.save(bookingToSave);
        return bookingMapper.toBookingDto(bookingSaved);
    }

    // READ ALL
    public List<BookingResponseDTO> findAll() {
        List<BookingEntity> bookings = bookingRepository.findAll();
        return bookingMapper.toBookingsDto(bookings);

    }

    // READ BY ID
    public Optional<BookingResponseDTO> findById(Integer id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::toBookingDto);
    }

    // UPDATE
    public Optional<BookingResponseDTO> update(Integer id, BookingRequestDTO bookingRequestDTO)
    {
        return bookingRepository.findById(id)
                .map(existingBooking -> {
                    if (existsBySpaceIdAndTimeOverlap(bookingRequestDTO.getIdSpace(),
                            bookingRequestDTO.getStartDateTime(), bookingRequestDTO.getEndDateTime())) {
                        throw new IllegalArgumentException("El espacio no está disponible en el horario solicitado.");
                    }
                    existingBooking.setIdSpace(bookingRequestDTO.getIdSpace());
                    existingBooking.setStartDateTime(bookingRequestDTO.getStartDateTime());
                    existingBooking.setEndDateTime(bookingRequestDTO.getEndDateTime());
                    BookingEntity updatedBooking = bookingRepository.save(existingBooking);
                    return bookingMapper.toBookingDto(updatedBooking);
                });
    }

    // Solo para actualizar el estado de la reserva
    public Optional<BookingResponseDTO> updateStatus(Integer id, String status) {
        return bookingRepository.findById(id)
                .map(existingBooking -> {
                    existingBooking.setStatus(BookingMapper.mapStatusToEnum(status));
                    BookingEntity updatedBooking = bookingRepository.save(existingBooking);
                    return bookingMapper.toBookingDto(updatedBooking);
                });
    }





}
