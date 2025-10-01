package com.eam.surfspace.domain.service.impl;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.service.BookingService;
import com.eam.surfspace.domain.service.SpaceService;
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
    private final MembershipService membershipService; // Servicio para validar membresía
    private final SpaceService spaceService;

    @Override
    public BookingResponseDTO save(BookingRequestDTO bookingRequestDTO) {
        log.info("Create a new booking for a client in the coworking space {}",
                bookingRequestDTO.getIdMembership());
        // TODO
        // Aqui debo llamar el método para validar que la membresia esté activa
            // traer el metodo desde el servicio de membresia (MembershipService)
        boolean isMembershipActive = true; // Simulación de la verificación de la membresía
        if (!isMembershipActive) {
            throw new IllegalArgumentException("La membresía no está activa. No se puede crear la reserva.");
        }

        // TODO
        // Aqui debo llamar el método para validar que el espacio a reservar esté disponible
            //
        boolean isSpaceAvailable = true; // Simulación de la verificación de disponibilidad del espacio
        if (!isSpaceAvailable) {
            throw new IllegalArgumentException("El espacio no está disponible en el horario solicitado.");
        }

        // Validar reglas de negocio respecto a las fechas y horas de la reserva
        validateTimes(bookingRequestDTO.getStartDateTime(), bookingRequestDTO.getEndDateTime());
        validateDuration(bookingRequestDTO.getStartDateTime(), bookingRequestDTO.getEndDateTime());

        // Crear reseeva
        BookingResponseDTO bookingResponseDTO = bookingDAO.save(bookingRequestDTO);
        log.info("Booking has been saved successfully");

        return bookingResponseDTO;
    }

    /***
     * Verifica las fechas y horas de la reserva : haciendo las siguientes validaciones
     *  Valida que las las fechas y horas no sean nulas
     *  Valida que fecha y hora de inicio sea posterior a la hora actual
     *  Valida que la hora de fin sea mayor 30 minudos de la hora de inicio
     *  Valida que la fecha de inicio no sea mayor a 3 meses de la fecha actual
     * @param start - fecha y hora de inicio
     * @param end - fecha y hora de fin
     * @throws IllegalArgumentException si alguna de las validaciones falla
     */
    private void validateTimes(LocalDateTime start, LocalDateTime end) {
        // Valida que las las fechas y horas no sean nulas
        if (start == null || end == null) {
            throw new IllegalArgumentException("Hora de Inicio y Hora de Fin son Obligatorias");
        }

        // Valida que fecha y hora de inicio sea posterior a la hora actual
        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser una fecha futura.");
        }

        // Valida que la hora de inicio sea mayor a 30 minutos de la hora actual
        if (!start.isAfter(LocalDateTime.now().plusMinutes(30))) {
            throw new IllegalArgumentException("Hora Inicio debe ser mayor a 30 minutos de la hora actual");
        }

        // Valida que la fecha de inicio no sea mayor a 3 meses de la fecha actual
        if(start.isAfter(LocalDateTime.now().plusMonths(3))){
            throw new IllegalArgumentException("La reserva no puede hacerse con más de 3 meses de anticipación.");
        }

    }

    /***
     * Valida la duración de la reserva
     * La duración mínima es de 1 hora
     * La duración máxima es de 8 horas
     * La duración debe ser en horas exactas
     * @param startDateTime
     * @param endDateTime
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
     * Valida que una reserva pueda ser modificada solo hasta 12h antes del inicio
     */
    private void validateModification(LocalDateTime start) {
        LocalDateTime now = LocalDateTime.now();

        if (start.isBefore(now.plusHours(12))) {
            throw new IllegalArgumentException("La reserva solo puede modificarse hasta 12 horas antes de la hora de inicio");
        }
    }



}
