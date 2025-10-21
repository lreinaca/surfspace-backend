package com.eam.surfspace.domain;

import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.dto.NotificationDTO;
import com.eam.surfspace.domain.service.impl.NotificationServiceImpl;
import com.eam.surfspace.persistence.dao.NotificationDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Notification Service Tests")
public class NotificationServiceTest {

    @Mock
    private NotificationDAO notificationDAO;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private BookingResponseDTO bookingResponse;

    @BeforeEach
    void setUp() {
        bookingResponse = new BookingResponseDTO(
                123, // bookingId
                1,
                1,
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().plusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0),
                "CONFIRMADA"
        );
    }

    @Test
    @DisplayName("sendConfirmation debe crear y persistir una notification con confirmation code")
    void testSendConfirmation() {
        // Preparar: el DAO devolverá exactamente el notification que se le pase
        when(notificationDAO.save(any(NotificationDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NotificationDTO result = notificationService.sendConfirmation(bookingResponse);

        assertThat(result).isNotNull();
        assertThat(result.getBookingId()).isEqualTo(bookingResponse.getBookingId());
        assertThat(result.getType()).isEqualTo("CONFIRMATION");
        assertThat(result.getConfirmationCode()).isNotNull();
        assertThat(result.getMessage()).contains("Su reserva ha sido confirmada");

        // El código debe incluir el prefijo BOOKING-
        assertThat(result.getConfirmationCode()).startsWith("BOOKING-");

        verify(notificationDAO).save(any(NotificationDTO.class));
    }

    @Test
    @DisplayName("sendReminder debe crear y persistir una notification de recordatorio")
    void testSendReminder() {
        when(notificationDAO.save(any(NotificationDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NotificationDTO result = notificationService.sendReminder(bookingResponse);

        assertThat(result).isNotNull();
        assertThat(result.getBookingId()).isEqualTo(bookingResponse.getBookingId());
        assertThat(result.getType()).isEqualTo("REMINDER");
        assertThat(result.getMessage()).contains("Recordatorio");
        // Debe incluir la hora local del inicio
        assertThat(result.getMessage()).contains(bookingResponse.getStartDateTime().toLocalTime().toString());

        verify(notificationDAO).save(any(NotificationDTO.class));
    }

    @Test
    @DisplayName("sendPaymentDue debe crear y persistir una notification de pago pendiente")
    void testSendPaymentDue() {
        when(notificationDAO.save(any(NotificationDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NotificationDTO result = notificationService.sendPaymentDue(bookingResponse.getBookingId());

        assertThat(result).isNotNull();
        assertThat(result.getBookingId()).isEqualTo(bookingResponse.getBookingId());
        assertThat(result.getType()).isEqualTo("PAYMENT_DUE");
        // comprobar que el mensaje contiene palabras clave esperadas (más robusto que usar contains con una regex)
        assertThat(result.getMessage()).contains("Advertencia").contains("pago");

        verify(notificationDAO).save(any(NotificationDTO.class));
    }

    @Test
    @DisplayName("getAllNotifications debe retornar la lista provista por el DAO")
    void testGetAllNotifications() {
        NotificationDTO n1 = new NotificationDTO();
        n1.setBookingId(1);
        n1.setType("CONFIRMATION");

        NotificationDTO n2 = new NotificationDTO();
        n2.setBookingId(2);
        n2.setType("REMINDER");

        List<NotificationDTO> list = Arrays.asList(n1, n2);
        when(notificationDAO.findAll()).thenReturn(list);

        List<NotificationDTO> result = notificationService.getAllNotifications();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).containsExactlyElementsOf(list);

        verify(notificationDAO).findAll();
    }

    @Test
    @DisplayName("generateConfirmationCode debe formatear correctamente incluso con bookingId nulo")
    void testGenerateConfirmationCode() {
        String code = notificationService.generateConfirmationCode(5);
        assertThat(code).startsWith("BOOKING-");
        // debe terminar con 4 dígitos del id
        assertThat(code).matches("BOOKING-\\d{8}-0005|");

        String codeNull = notificationService.generateConfirmationCode(null);
        assertThat(codeNull).endsWith("0000");
    }

}
