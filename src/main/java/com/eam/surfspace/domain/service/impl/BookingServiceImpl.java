package com.eam.surfspace.domain.service.impl;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.service.BookingService;
import com.eam.surfspace.domain.service.MembershipService;
import com.eam.surfspace.domain.service.SpaceService;
import com.eam.surfspace.persistence.dao.BookingDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j

public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO;
    private final MembershipService membershipService;
    private final SpaceService spaceService;

    @Override
    public BookingResponseDTO save(BookingRequestDTO bookingRequestDTO) {
        log.info("Create a new booking for a client in the coworking space {}",
                bookingRequestDTO.getIdMembership() + " " + bookingRequestDTO.getIdSpace());

        // Validar campos obligatorios
        validateObligatoryFields(bookingRequestDTO);

        // TODO : Validar que la membresía esté activa
        // Aqui debo llamar el método para validar que la membresia esté activa
            // traer el metodo desde el servicio de membresia (MembershipService)
        boolean isMembershipActive = true; // Simulación de la verificación de la membresía
        if (!isMembershipActive) {
            throw new IllegalArgumentException("La membresía no está activa. No se puede crear la reserva.");
        }

        // Validar que el espacio esté disponible en el horario solicitado
        boolean isSpaceAvailable = spaceService.isSpaceAvailable(
                bookingRequestDTO.getIdSpace(),
                bookingRequestDTO.getStartDateTime(),
                bookingRequestDTO.getEndDateTime()
        );
        if (!isSpaceAvailable) {
            throw new IllegalArgumentException("El espacio no está disponible en el horario solicitado.");
        }

        // Validar reglas de negocio respecto a las fechas y horas de la reserva
        validateTimes(bookingRequestDTO.getStartDateTime(), bookingRequestDTO.getEndDateTime());
        validateDuration(bookingRequestDTO.getStartDateTime(), bookingRequestDTO.getEndDateTime());


        // Crear reserva
        BookingResponseDTO bookingResponseDTO = bookingDAO.save(bookingRequestDTO);
        log.info("Booking has been saved successfully");

        return bookingResponseDTO;
    }

    private void validateObligatoryFields(BookingRequestDTO bookingRequestDTO) {
        // Valida que el ID de la membresía no sea nulo
        if (bookingRequestDTO.getIdMembership() == null || bookingRequestDTO.getIdMembership() <= 0) {
            throw new IllegalArgumentException("El ID de la membresía no puede estar vacío o ser nulo.");
        }

        // Valida que el ID del espacio no sea nulo
        if (bookingRequestDTO.getIdSpace() == null || bookingRequestDTO.getIdSpace().toString().isEmpty()) {
            throw new IllegalArgumentException("El ID del espacio no puede estar vacío o ser nulo.");
        }
        // Valida que las fechas y horas no sean nulas
        if (bookingRequestDTO.getStartDateTime() == null || bookingRequestDTO.getEndDateTime() == null
            || bookingRequestDTO.getStartDateTime().toString().isEmpty() || bookingRequestDTO.getEndDateTime().toString().isEmpty()) {
            throw new IllegalArgumentException("Hora de Inicio y Hora de Fin son Obligatorias");
        }

    }
    /***
     * Verifica las fechas y horas de la reserva : haciendo las siguientes validaciones
     *  Valida que fecha y hora de inicio sea posterior a la hora actual
     *  Valida que la hora de fin sea mayor 30 minudos de la hora de inicio
     *  Valida que la fecha de inicio no sea mayor a 3 meses de la fecha actual
     * @param start - fecha y hora de inicio
     * @param end - fecha y hora de fin
     * @throws IllegalArgumentException si alguna de las validaciones falla
     */
    private void validateTimes(LocalDateTime start, LocalDateTime end) {

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


    /***
     * Obtener todas las reservas
     * @return Lista de reservas
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {
        log.debug("Retrieving all bookings");
        return bookingDAO.findAll();
    }

    /***
     * Obtener una reserva por su ID
     * @param id - ID de la reserva
     * @return Reserva encontrada
     * @throws IllegalArgumentException si el ID es inválido o no se encuentra la reserva
     */
    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Integer id) {
        log.debug("Retrieving booking by ID: {}", id);
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de reserva inválido.");
        }
        return bookingDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Reserva con ID " + id + " no encontrada."));
    }

    /***
     * Actualizar una reserva existente
     * @param id - ID de la reserva a actualizar
     * @param bookingRequestDTO - Datos actualizados de la reserva
     * @return Reserva actualizada
     * @throws IllegalArgumentException si el ID es inválido, no se encuentra la reserva,
     *                                  la membresía no está activa, el espacio no está disponible,
     *                                  o las validaciones de tiempo fallan
     */
    @Override
    public BookingResponseDTO update(Integer id, BookingRequestDTO bookingRequestDTO) {
        log.info("Updating booking with ID: {}", id);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de reserva inválido.");
        }

        BookingResponseDTO existingBooking = bookingDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva con ID " + id + " no encontrada."));

        // Validar que la reserva pueda ser modificada
        validateModification(existingBooking.getStartDateTime());

        // Validar que la membresía esté activa
        boolean isMembershipActive = true; // Simulación de la verificación de la membresía
        if (!isMembershipActive) {
            throw new IllegalArgumentException("La membresía no está activa. No se puede actualizar la reserva.");
        }

        // Validar que el espacio esté disponible en el horario solicitado
        boolean isSpaceAvailable = spaceService.isSpaceAvailable(
                bookingRequestDTO.getIdSpace(),
                bookingRequestDTO.getStartDateTime(),
                bookingRequestDTO.getEndDateTime()
        );
        if (!isSpaceAvailable) {
            throw new IllegalArgumentException("El espacio no está disponible en el horario solicitado.");
        }

        // Validar reglas de negocio respecto a las fechas y horas de la reserva
        validateTimes(bookingRequestDTO.getStartDateTime(), bookingRequestDTO.getEndDateTime());
        validateDuration(bookingRequestDTO.getStartDateTime(), bookingRequestDTO.getEndDateTime());

        // Actualizar la reserva
        existingBooking.setIdSpace(bookingRequestDTO.getIdSpace());
        existingBooking.setStartDateTime(bookingRequestDTO.getStartDateTime());
        existingBooking.setEndDateTime(bookingRequestDTO.getEndDateTime());

        Optional<BookingResponseDTO> updatedBooking = bookingDAO.update(id, bookingRequestDTO);
        log.info("Booking with ID {} has been updated successfully", id);
        return updatedBooking.orElse(null);
    }

    /***
     * Cancelar una reserva existente
     * @param id - ID de la reserva a cancelar
     * @throws IllegalArgumentException si el ID es inválido, no se encuentra la reserva,
     *                                  o la reserva no puede ser cancelada
     */
    @Override
    public void cancelBooking(Integer id) {
        log.info("Cancelling booking with ID: {}", id);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de reserva inválido.");
        }

        BookingResponseDTO existingBooking = bookingDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva con ID " + id + " no encontrada."));

        // Validar que la reserva pueda ser cancelada (solo si faltan más de 24 horas para el inicio)
        if (existingBooking.getStartDateTime().isBefore(LocalDateTime.now().plusHours(24))) {
            throw new IllegalArgumentException("La reserva solo puede eliminarse si faltan más de 24 horas para la hora de inicio.");
        }

        // Cambiar estado de la reserva a CANCELADA
        existingBooking.setStatus("CANCELADA");
        bookingDAO.updateStatus(id,existingBooking.getStatus());
        log.info("Booking with ID {} has been cancelled successfully", id);
    }
}
