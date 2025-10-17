package com.eam.surfspace.domain.service.impl;


import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.service.BookingService;
import com.eam.surfspace.domain.service.NotificationService;
import com.eam.surfspace.persistence.dao.NotificationDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationScheduler {
    private final NotificationService notificationService;
    private final NotificationDAO notificationDAO;

    // Ejecutar cada 15 minutos para verificar pagos próximos a vencer
    @Scheduled(cron = "0 */15 * * * *")
    public void sendPaymentDueWarnings() {
        log.info("Verificando reservas con pagos próximos a vencer...");

        // Buscar reservas cuyo pago vence en menos de 1 hora
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<BookingResponseDTO> bookings = notificationDAO.findBookingsWithPaymentDue(oneHourAgo);


        log.info("Encontradas {} reservas con pagos próximos a vencer", bookings.size());

        for (BookingResponseDTO booking : bookings) {
            try {
                notificationService.sendPaymentDue(booking.getBookingId());
                log.info("Aviso de pago enviado para reserva {}", booking.getBookingId());
            } catch (Exception e) {
                log.error("Error al enviar aviso de pago para reserva {}", booking.getBookingId(), e);
            }
        }
    }

    // Ejecutar cada hora para recordatorios 24h antes
    @Scheduled(cron = "0 0 * * * *")
    public void sendReminders() {
        log.info("Verificando reservas que necesitan recordatorio...");

        LocalDateTime now = LocalDateTime.now();
        List<BookingResponseDTO> bookings = notificationDAO.findBookingsNeedingReminder(now);

        log.info("Encontradas {} reservas para enviar recordatorio", bookings.size());

        for (BookingResponseDTO booking : bookings) {
            try {
                notificationService.sendReminder(booking);
                log.info("Recordatorio enviado para reserva {}", booking.getBookingId());
            } catch (Exception e) {
                log.error("Error al enviar recordatorio para reserva {}", booking.getBookingId(), e);
            }
        }
    }
}

