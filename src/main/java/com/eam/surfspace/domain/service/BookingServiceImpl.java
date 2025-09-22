package com.eam.surfspace.domain.service;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.persistence.dao.BookingDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j

public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO;

    @Override
    public BookingResponseDTO save(BookingRequestDTO bookingRequestDTO) {
        log.info("Create a new booking for a client in the coworking space {}",
                bookingRequestDTO.getIdMembership());

        // Validar reglas de negocio respecto a las horas
        validateTimes(bookingRequestDTO.getStartDateTime(), bookingRequestDTO.getEndDateTime());
        validateDuration(bookingRequestDTO.getStartDateTime(), bookingRequestDTO.getEndDateTime());
        validateBookingDate(bookingRequestDTO.getStartDateTime());

        // crear reseeva
        BookingResponseDTO bookingResponseDTO = bookingDAO.save(bookingRequestDTO);
        log.info("Booking has been saved successfully");

        return bookingResponseDTO;
    }

    /**
     * Validación: hora de inicio < hora de fin
     */
    private void validateTimes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Hora de Inicio y Hora de Fin son Obligatorias");
        }
        // Hora fin debe ser estrictamente mayor que hora inicio
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Hora Fin debe ser mayor a la hora de inicio");
        }

        // La fecha de reserva debe ser una fecha futura, no puede reservar en el pasado
        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser una fecha futura.");
        }
    }

    /**
     * Valida la duración de una reserva:
     * - Mínimo 1 hora
     * - Máximo 8 horas
     * - Debe ser múltiplo de horas exacto
     */
    private void validateDuration(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Duration duration = Duration.between(startDateTime, endDateTime);
        if (duration.toHours() < 1) {
            throw new IllegalArgumentException("La duración mínima es 1 hora.");
        }
        if (duration.toHours() > 8) {
            throw new IllegalArgumentException("La duración máxima es 8 horas.");
        }

        if (duration.toMinutes() % 60 != 0) {
            throw new IllegalArgumentException("La duración debe ser en horas exactas.");
        }
    }

    /**
     * Validación: anticipación mínima (30 min) y máxima (3 meses)
     */
    private void validateBookingDate(LocalDateTime bookingDate) {
        LocalDateTime now = LocalDateTime.now();
        if(bookingDate.isBefore(now.plusMinutes(30))){
            throw new IllegalArgumentException("La reserva debe hacerse con al menos 30 minutos de anticipación.");
        }

        if(bookingDate.isAfter(now.plusMonths(3))){
            throw new IllegalArgumentException("La reserva no puede hacerse con más de 3 meses de anticipación.");
        }
    }

    /**
     * Valida que una reserva pueda ser modificada solo hasta 12h antes del inicio
     */
    private void validateModification(LocalDateTime start) {
        LocalDateTime now = LocalDateTime.now();

        if (start.isBefore(now.plusHours(12))) {
            throw new IllegalArgumentException("La reserva solo puede modificarse hasta 12 horas antes de la hora de inicio");
        }
    }



}
