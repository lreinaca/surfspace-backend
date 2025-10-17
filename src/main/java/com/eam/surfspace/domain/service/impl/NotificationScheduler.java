package com.eam.surfspace.domain.service.impl;


import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.service.BookingService;
import com.eam.surfspace.domain.service.NotificationService;
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
    private final BookingService bookingService;

    // Se ejecuta cada hora
    @Scheduled(cron = "0 0 * * * *")
    public void sendReminders() {
        log.info("Checking bookings for 24-hour reminders");
        LocalDateTime tomorrow = LocalDateTime.now().plusHours(24);

        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        bookings.stream()
                .filter(b -> b.getStartDateTime().isAfter(tomorrow.minusMinutes(30))
                        && b.getStartDateTime().isBefore(tomorrow.plusMinutes(30)))
                .forEach(b -> {
                    log.info("Sending reminder for booking {}", b.getBookingId());
                    notificationService.sendReminder(b.getBookingId());
                });
    }

    // Se ejecuta cada 15 minutos
    @Scheduled(cron = "0 */15 * * * *")
    public void checkPaymentDue() {
        log.info("Checking for pending payments");
        // Aquí integras con PaymentService para verificar pagos pendientes
        // y enviar notificationService.sendPaymentDue(bookingId) si falta < 1h
    }
}

