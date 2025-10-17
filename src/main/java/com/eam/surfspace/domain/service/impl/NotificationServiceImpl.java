package com.eam.surfspace.domain.service.impl;

import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.dto.NotificationDTO;
import com.eam.surfspace.domain.service.BookingService;
import com.eam.surfspace.domain.service.NotificationService;
import com.eam.surfspace.persistence.dao.NotificationDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationDAO notificationDAO;
    private final BookingService bookingService;

    @Override
    public NotificationDTO sendConfirmation(Integer bookingId) {
        BookingResponseDTO booking = bookingService.getBookingById(bookingId);
        String code = generateConfirmationCode();

        NotificationDTO notification = new NotificationDTO();
        notification.setBookingId(bookingId);
        notification.setType("CONFIRMATION");
        notification.setMessage("Su reserva ha sido confirmada. Código: " + code);
        notification.setConfirmationCode(code);
        notification.setSentAt(LocalDateTime.now());

        log.info("Sending confirmation for booking {}: {}", bookingId, code);
        return notificationDAO.save(notification);
    }

    @Override
    public NotificationDTO sendReminder(Integer bookingId) {
        BookingResponseDTO booking = bookingService.getBookingById(bookingId);

        NotificationDTO notification = new NotificationDTO();
        notification.setBookingId(bookingId);
        notification.setType("REMINDER");
        notification.setMessage("Recordatorio: su reserva es mañana a las " +
                booking.getStartDateTime().toLocalTime());
        notification.setSentAt(LocalDateTime.now());

        log.info("Sending reminder for booking {}", bookingId);
        return notificationDAO.save(notification);
    }

    @Override
    public NotificationDTO sendPaymentDue(Integer bookingId) {
        NotificationDTO notification = new NotificationDTO();
        notification.setBookingId(bookingId);
        notification.setType("PAYMENT_DUE");
        notification.setMessage("Advertencia: falta menos de 1 hora para completar el pago de su reserva");
        notification.setSentAt(LocalDateTime.now());

        log.warn("Sending payment due warning for booking {}", bookingId);
        return notificationDAO.save(notification);
    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        return notificationDAO.findAll();
    }

    @Override
    public String generateConfirmationCode() {
        String code;
        int attempts = 0;
        do {
            // Formato: SURF-YYYYMMDD-XXXX donde XXXX es secuencial del día
            String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            long count = notificationDAO.countByConfirmationCodeStartingWith("SURF-" + datePrefix);

            code = String.format("SURF-%s-%04d", datePrefix, count + 1);
            attempts++;
        } while (notificationDAO.existsByConfirmationCode(code) && attempts < 10);

        if (attempts >= 10) {
            throw new RuntimeException("No se pudo generar un código único");
        }

        return code;
    }

}
