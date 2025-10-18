package com.eam.surfspace.domain;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.service.MembershipService;
import com.eam.surfspace.domain.service.SpaceService;
import com.eam.surfspace.domain.service.impl.BookingServiceImpl;
import com.eam.surfspace.persistence.dao.BookingDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Booking Service Test")
public class BookingServiceTest {

    @Mock
    private BookingDAO bookingDAO;

    @Mock
    private MembershipService membershipService;

    @Mock
    private SpaceService spaceService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingRequestDTO validBookingDTO;
    private BookingResponseDTO validBookingResponseDTO;
    private Integer spaceId;
    private Integer membershipId;

    @BeforeEach
    void setUp() {
        spaceId = 1;
        membershipId = 1;

        // Fecha futura (2 días desde ahora)
        LocalDateTime futureStart = LocalDateTime.now().plusDays(2).withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime futureEnd = futureStart.plusHours(2);

        validBookingDTO = new BookingRequestDTO(
                membershipId,
                spaceId,
                futureStart,
                futureEnd,
                "PENDIENTE"
        );

        validBookingResponseDTO = new BookingResponseDTO(
                1,
                membershipId,
                spaceId,
                futureStart,
                futureEnd,
                "PENDIENTE"
        );
    }

    // ========== TESTS PARA EL MÉTODO SAVE ==========

    @Test
    @DisplayName("CREATE - Debe crear una reserva cuando los datos son válidos")
    void testCreateBooking_ValidData() {
        // Configurar mocks
        when(spaceService.isSpaceAvailable(validBookingDTO.getIdSpace(), validBookingDTO.getStartDateTime(), validBookingDTO.getEndDateTime())).thenReturn(true);
        when(bookingDAO.save(any(BookingRequestDTO.class))).thenReturn(validBookingResponseDTO);

        // Ejecutar
        BookingResponseDTO result = bookingService.save(validBookingDTO);

        // Verificar
        assertThat(result).isNotNull();
        assertThat(result.getBookingId()).isEqualTo(1);
        assertThat(result.getIdMembership()).isEqualTo(membershipId);
        assertThat(result.getIdSpace()).isEqualTo(spaceId);

        verify(spaceService).isSpaceAvailable(eq(spaceId), any(), any());
        verify(bookingDAO).save(validBookingDTO);
    }

    @Test
    @DisplayName("CREATE - Debe lanzar excepción cuando el espacio no está disponible")
    void testCreateBooking_SpaceNotAvailable() {
        // Configurar mock
        when(spaceService.isSpaceAvailable(validBookingDTO.getIdSpace(), validBookingDTO.getStartDateTime(), validBookingDTO.getEndDateTime())).thenReturn(false);

        // Verificar que lanza excepción
        assertThatThrownBy(() -> bookingService.save(validBookingDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El espacio no está disponible");

        verify(bookingDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Debe lanzar excepción cuando la fecha de inicio es pasada")
    void testCreateBooking_StartDateInPast() {
        LocalDateTime pastStart = LocalDateTime.now().minusDays(1);
        LocalDateTime pastEnd = pastStart.plusHours(2);

        BookingRequestDTO pastBooking = new BookingRequestDTO(
                membershipId, spaceId, pastStart, pastEnd, "PENDIENTE"
        );

        when(spaceService.isSpaceAvailable(pastBooking.getIdSpace(), pastBooking.getStartDateTime(), pastBooking.getEndDateTime())).thenReturn(true);

        assertThatThrownBy(() -> bookingService.save(pastBooking))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fecha de inicio debe ser una fecha futura");

        verify(bookingDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Debe lanzar excepción cuando la duración es menor a 1 hora")
    void testCreateBooking_DurationTooShort() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusMinutes(30);

        BookingRequestDTO shortBooking = new BookingRequestDTO(
                membershipId, spaceId, start, end, "PENDIENTE"
        );

        when(spaceService.isSpaceAvailable(shortBooking.getIdSpace(), shortBooking.getStartDateTime(), shortBooking.getEndDateTime())).thenReturn(true);

        assertThatThrownBy(() -> bookingService.save(shortBooking))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("duración mínima es 1 hora");

        verify(bookingDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Debe lanzar excepción cuando la duración es mayor a 8 horas")
    void testCreateBooking_DurationTooLong() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(9);

        BookingRequestDTO longBooking = new BookingRequestDTO(
                membershipId, spaceId, start, end, "PENDIENTE"
        );

        when(spaceService.isSpaceAvailable(validBookingDTO.getIdSpace(),start, end)).thenReturn(true);

        assertThatThrownBy(() -> bookingService.save(longBooking))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("duración máxima es 8 horas");

        verify(bookingDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Debe lanzar excepción cuando la reserva es con más de 3 meses de anticipación")
    void testCreateBooking_TooFarInFuture() {
        LocalDateTime farStart = LocalDateTime.now().plusMonths(4);
        LocalDateTime farEnd = farStart.plusHours(2);

        BookingRequestDTO farBooking = new BookingRequestDTO(
                membershipId, spaceId, farStart, farEnd, "PENDIENTE"
        );

        when(spaceService.isSpaceAvailable(validBookingDTO.getIdSpace(), farStart, farEnd)).thenReturn(true);

        assertThatThrownBy(() -> bookingService.save(farBooking))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("más de 3 meses de anticipación");

        verify(bookingDAO, never()).save(any());
    }

    // ========== TESTS PARA EL MÉTODO GET ALL BOOKINGS ==========

    @Test
    @DisplayName("GET ALL - Debe retornar todas las reservas")
    void testGetAllBookings_Success() {
        // Preparar datos
        List<BookingResponseDTO> expectedBookings = Arrays.asList(
                validBookingResponseDTO,
                new BookingResponseDTO(2, 2, 2,
                        LocalDateTime.now().plusDays(3),
                        LocalDateTime.now().plusDays(3).plusHours(3),
                        "CONFIRMADA")
        );

        when(bookingDAO.findAll()).thenReturn(expectedBookings);

        // Ejecutar
        List<BookingResponseDTO> result = bookingService.getAllBookings();

        // Verificar
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        verify(bookingDAO).findAll();
    }

    @Test
    @DisplayName("GET ALL - Debe retornar lista vacía cuando no hay reservas")
    void testGetAllBookings_EmptyList() {
        when(bookingDAO.findAll()).thenReturn(Arrays.asList());

        List<BookingResponseDTO> result = bookingService.getAllBookings();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);
        verify(bookingDAO).findAll();
    }

    // ========== TESTS PARA EL MÉTODO GET BY ID ==========

    @Test
    @DisplayName("GET BY ID - Debe retornar una reserva cuando existe")
    void testGetBookingById_Success() {
        Integer bookingId = 1;
        when(bookingDAO.findById(bookingId)).thenReturn(Optional.of(validBookingResponseDTO));

        BookingResponseDTO result = bookingService.getBookingById(bookingId);

        assertThat(result).isNotNull();
        assertThat(result.getBookingId()).isEqualTo(bookingId);
        verify(bookingDAO).findById(bookingId);
    }

    @Test
    @DisplayName("GET BY ID - Debe lanzar excepción cuando la reserva no existe")
    void testGetBookingById_NotFound() {
        Integer bookingId = 999;
        when(bookingDAO.findById(bookingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBookingById(bookingId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("no encontrada");

        verify(bookingDAO).findById(bookingId);
    }

    @Test
    @DisplayName("GET BY ID - Debe lanzar excepción cuando el ID es inválido")
    void testGetBookingById_InvalidId() {
        assertThatThrownBy(() -> bookingService.getBookingById(null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("ID de reserva inválido");

        assertThatThrownBy(() -> bookingService.getBookingById(0))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("ID de reserva inválido");

        verify(bookingDAO, never()).findById(any());
    }

    // ========== TESTS PARA EL MÉTODO UPDATE ==========

    @Test
    @DisplayName("UPDATE - Debe actualizar una reserva cuando los datos son válidos")
    void testUpdateBooking_Success() {
        Integer bookingId = 1;
        LocalDateTime newStart = LocalDateTime.now().plusDays(5);
        LocalDateTime newEnd = newStart.plusHours(3);

        BookingRequestDTO updateDTO = new BookingRequestDTO(
                membershipId, spaceId, newStart, newEnd, "CONFIRMADA"
        );

        BookingResponseDTO updatedResponse = new BookingResponseDTO(
                bookingId, membershipId, spaceId, newStart, newEnd, "CONFIRMADA"
        );

        when(bookingDAO.findById(bookingId)).thenReturn(Optional.of(validBookingResponseDTO));
        when(spaceService.isSpaceAvailable(updatedResponse.getIdSpace(), updatedResponse.getStartDateTime(), updatedResponse.getEndDateTime())).thenReturn(true);
        when(bookingDAO.update(eq(bookingId), any())).thenReturn(Optional.of(updatedResponse));

        BookingResponseDTO result = bookingService.update(bookingId, updateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getBookingId()).isEqualTo(bookingId);
        verify(bookingDAO).update(anyInt(), any(BookingRequestDTO.class));
        verify(bookingDAO).update(eq(bookingId), eq(updateDTO));
    }

    @Test
    @DisplayName("UPDATE - Debe lanzar excepción cuando la reserva no existe")
    void testUpdateBooking_NotFound() {
        Integer bookingId = 999;
        when(bookingDAO.findById(bookingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.update(bookingId, validBookingDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no encontrada");

        verify(bookingDAO).findById(bookingId);
        verify(bookingDAO, never()).update(any(), any());
    }

    @Test
    @DisplayName("UPDATE - Debe lanzar excepción cuando faltan menos de 12 horas para el inicio")
    void testUpdateBooking_TooCloseToStart() {
        Integer bookingId = 1;
        LocalDateTime soonStart = LocalDateTime.now().plusHours(10);
        LocalDateTime soonEnd = soonStart.plusHours(2);

        BookingResponseDTO soonBooking = new BookingResponseDTO(
                bookingId, membershipId, spaceId, soonStart, soonEnd, "PENDIENTE"
        );

        when(bookingDAO.findById(bookingId)).thenReturn(Optional.of(soonBooking));

        assertThatThrownBy(() -> bookingService.update(bookingId, validBookingDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("12 horas antes");

        verify(bookingDAO).findById(bookingId);
        verify(bookingDAO, never()).update(any(), any());
    }

    @Test
    @DisplayName("UPDATE - Debe lanzar excepción cuando el ID es inválido")
    void testUpdateBooking_InvalidId() {
        assertThatThrownBy(() -> bookingService.update(null, validBookingDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID de reserva inválido");

        assertThatThrownBy(() -> bookingService.update(-1, validBookingDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID de reserva inválido");

        verify(bookingDAO, never()).update(any(), any());
    }

    // ========== TESTS PARA EL MÉTODO DELETE ==========

    @Test
    @DisplayName("CANCEL - Debe cancelar una reserva cuando faltan más de 24 horas")
    void testDeleteBooking_Success() {
        Integer bookingId = 1;
        LocalDateTime farStart = LocalDateTime.now().plusDays(2);
        LocalDateTime farEnd = farStart.plusHours(2);

        BookingResponseDTO bookingToCancel = new BookingResponseDTO(
                bookingId, membershipId, spaceId, farStart, farEnd, "CANCELADO"
        );

        Optional<BookingResponseDTO> optionalBooking = Optional.of(bookingToCancel);


        when(bookingDAO.findById(bookingId)).thenReturn(Optional.of(bookingToCancel));
        when(bookingDAO.updateStatus(bookingId,bookingToCancel.getStatus())).thenReturn(optionalBooking);

        bookingService.cancelBooking(bookingId);

        verify(bookingDAO).findById(bookingId);
        verify(bookingDAO).updateStatus(bookingId,bookingToCancel.getStatus());
    }

    @Test
    @DisplayName("CANCEL - Debe lanzar excepción cuando la reserva no existe")
    void testDeleteBooking_NotFound() {
        Integer bookingId = 999;
        when(bookingDAO.findById(bookingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelBooking(bookingId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no encontrada");

        verify(bookingDAO).findById(bookingId);
        verify(bookingDAO, never()).updateStatus(any(), any());
    }

    @Test
    @DisplayName("CANCEL - Debe lanzar excepción cuando faltan menos de 24 horas para el inicio")
    void testCancelBooking_TooCloseToStart() {
        Integer bookingId = 1;
        LocalDateTime soonStart = LocalDateTime.now().plusHours(20);
        LocalDateTime soonEnd = soonStart.plusHours(2);

        BookingResponseDTO soonBooking = new BookingResponseDTO(
                bookingId, membershipId, spaceId, soonStart, soonEnd, "CONFIRMADA"
        );

        when(bookingDAO.findById(bookingId)).thenReturn(Optional.of(soonBooking));

        assertThatThrownBy(() -> bookingService.cancelBooking(bookingId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("24 horas");

        verify(bookingDAO).findById(bookingId);
        verify(bookingDAO, never()).updateStatus(any(), any());
    }

    @Test
    @DisplayName("CANCEL - Debe lanzar excepción cuando el ID es inválido")
    void testCancelBooking_InvalidId() {
        assertThatThrownBy(() -> bookingService.cancelBooking(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID de reserva inválido");

        assertThatThrownBy(() -> bookingService.cancelBooking(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID de reserva inválido");

        verify(bookingDAO, never()).updateStatus(any(),any());
    }
}
