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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Booking Service Edge Cases Tests")
public class BookingServiceEdgeCasesTest {

    @Mock
    private BookingDAO bookingDAO;

    @Mock
    private MembershipService membershipService;

    @Mock
    private SpaceService spaceService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Integer membershipId;
    private Integer spaceId;

    @BeforeEach
    void setUp() {
        membershipId = 1;
        spaceId = 1;
    }

    @Test
    @DisplayName("SAVE - debe fallar cuando start o end son nulos")
    void saveShouldFailWhenTimesNull() {
        BookingRequestDTO dto = new BookingRequestDTO(membershipId, spaceId, null, null, "PENDIENTE");

        // Asegurar que spaceService devuelva true para llegar a las validaciones de tiempo
        when(spaceService.isSpaceAvailable(anyInt(), nullable(LocalDateTime.class), nullable(LocalDateTime.class))).thenReturn(true);

        assertThatThrownBy(() -> bookingService.save(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Hora de Inicio y Hora de Fin son Obligatorias");
    }

    @Test
    @DisplayName("SAVE - debe fallar cuando el inicio no es mayor a 30 minutos desde ahora")
    void saveShouldFailWhenStartWithin30Minutes() {
        LocalDateTime start = LocalDateTime.now().plusMinutes(30); // exactamente 30 -> debe fallar (no es after)
        LocalDateTime end = start.plusHours(1);
        BookingRequestDTO dto = new BookingRequestDTO(membershipId, spaceId, start, end, "PENDIENTE");

        when(spaceService.isSpaceAvailable(anyInt(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(true);

        assertThatThrownBy(() -> bookingService.save(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Hora Inicio debe ser mayor a 30 minutos");
    }

    @Test
    @DisplayName("SAVE - debe fallar cuando start está a más de 3 meses")
    void saveShouldFailWhenStartMoreThan3Months() {
        LocalDateTime start = LocalDateTime.now().plusMonths(4);
        LocalDateTime end = start.plusHours(2);
        BookingRequestDTO dto = new BookingRequestDTO(membershipId, spaceId, start, end, "PENDIENTE");

        when(spaceService.isSpaceAvailable(anyInt(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(true);

        assertThatThrownBy(() -> bookingService.save(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("más de 3 meses");
    }

    @Test
    @DisplayName("SAVE - debe fallar cuando la duración no es un número entero de horas")
    void saveShouldFailWhenDurationNotWholeHours() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusMinutes(90); // 1.5 horas
        BookingRequestDTO dto = new BookingRequestDTO(membershipId, spaceId, start, end, "PENDIENTE");

        when(spaceService.isSpaceAvailable(anyInt(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(true);

        assertThatThrownBy(() -> bookingService.save(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La duración debe ser en horas exactas");
    }

    @Test
    @DisplayName("UPDATE - debe fallar cuando la modificación está muy cerca del inicio (validateModification)")
    void updateShouldFailWhenTooCloseToStart() {
        Integer bookingId = 1;
        LocalDateTime soonStart = LocalDateTime.now().plusHours(10);
        LocalDateTime soonEnd = soonStart.plusHours(2);

        BookingResponseDTO existing = new BookingResponseDTO(bookingId, membershipId, spaceId, soonStart, soonEnd, "PENDIENTE");
        when(bookingDAO.findById(bookingId)).thenReturn(Optional.of(existing));

        BookingRequestDTO updateDto = new BookingRequestDTO(membershipId, spaceId, soonStart.plusDays(1), soonEnd.plusDays(1), "CONFIRMADA");

        assertThatThrownBy(() -> bookingService.update(bookingId, updateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("hasta 12 horas antes");
    }

    @Test
    @DisplayName("CANCEL - debe fallar si el id es inválido")
    void cancelShouldFailOnInvalidId() {
        assertThatThrownBy(() -> bookingService.cancelBooking(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID de reserva inválido");

        assertThatThrownBy(() -> bookingService.cancelBooking(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID de reserva inválido");
    }

}
