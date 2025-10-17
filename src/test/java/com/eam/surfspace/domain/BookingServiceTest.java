package com.eam.surfspace.domain;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.service.BookingService;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Booking Service Test")

public class BookingServiceTest {

    // Mock Objeto virtual simulado
    @Mock
    private BookingDAO bookingDAO;
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    // Datos de la prueba
    private BookingRequestDTO validBookingDTO;
    private BookingResponseDTO validBookingResponseDTO;
    private Integer spaceId;
    private Integer membershipId;

    // Configuración inicial antes de cada prueba
    @BeforeEach
    void setUp() {
        // Inicializar datos de prueba
        spaceId = 1;
        membershipId = 1;
        validBookingDTO = new BookingRequestDTO(
                membershipId,
                spaceId,
                LocalDateTime.of(2024, 7, 1, 9, 0),
                LocalDateTime.of(2024, 7, 1, 11, 0),
                "PENDIENTE"
        );
    }

    @Test
    @DisplayName("CREATE - Should create a booking when data is valid")
    void testCreateBooking_ValidData() {
        BookingResponseDTO expectedBooking = new BookingResponseDTO(
                1,
                validBookingDTO.getIdMembership(),
                validBookingDTO.getIdSpace(),
                validBookingDTO.getStartDateTime(),
                validBookingDTO.getEndDateTime(),
                validBookingDTO.getStatus()
        );

        // Mock del comportamiento del bookingservice
        when(bookingService.save(validBookingDTO)).thenReturn(expectedBooking);

        // Mock del comportamiento del bookingDAO
        when(bookingDAO.save(validBookingDTO)).thenReturn(expectedBooking);

        // Llamada al método a probar
        BookingResponseDTO createdBooking = bookingService.save(validBookingDTO);

        // Verificaciones
        assertThat(createdBooking != null);
        assertThat(createdBooking.getBookingId()).isEqualTo(expectedBooking.getBookingId());
        assertThat(createdBooking.getIdMembership()).isEqualTo(expectedBooking.getIdMembership());
        assertThat(createdBooking.getIdSpace()).isEqualTo(expectedBooking.getIdSpace());
        assertThat(createdBooking.getStartDateTime()).isEqualTo(expectedBooking.getStartDateTime());
        assertThat(createdBooking.getEndDateTime()).isEqualTo(expectedBooking.getEndDateTime());
        assertThat(createdBooking.getStatus()).isEqualTo(expectedBooking.getStatus());


        // verificar que los metodos se llamaron al menos una vez
        verify(bookingService).save(validBookingDTO);
        verify(bookingDAO).save(validBookingDTO);
    }

}
