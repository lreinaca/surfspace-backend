package com.eam.surfspace.domain.service.impl;

import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.dto.NotificationDTO;
import com.eam.surfspace.domain.service.NotificationService;
import com.eam.surfspace.persistence.dao.NotificationDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationDAO notificationDAO;

    @Override
    public NotificationDTO sendConfirmation(BookingResponseDTO bookingResponseDTO) {
        String code = generateConfirmationCode(bookingResponseDTO.getBookingId());

        NotificationDTO notification = new NotificationDTO();
        notification.setBookingId(bookingResponseDTO.getBookingId());
        notification.setType("CONFIRMATION");
        notification.setMessage("Su reserva ha sido confirmada. Código: " + code);
        notification.setConfirmationCode(code);
        notification.setSentAt(LocalDateTime.now());

        log.info("Sending confirmation for booking {}: {}", bookingResponseDTO.getBookingId(), code);
        return notificationDAO.save(notification);
    }

    @Override
    public NotificationDTO sendReminder(BookingResponseDTO bookingResponseDTO) {

        NotificationDTO notification = new NotificationDTO();
        notification.setBookingId(bookingResponseDTO.getBookingId());
        notification.setType("REMINDER");
        notification.setMessage("Recordatorio: su reserva es mañana a las " +
                bookingResponseDTO.getStartDateTime().toLocalTime());
        notification.setSentAt(LocalDateTime.now());

        log.info("Sending reminder for booking {}", bookingResponseDTO.getBookingId());
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
    public String generateConfirmationCode(Integer bookingId) {
        if (bookingId == null) {
            bookingId = 0;
        }
        String code;
            // Formato: BOOKING-YYYYMMDD-bookingId
            String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            code = String.format("BOOKING-%s-%04d", datePrefix, bookingId);
            return code;
    }

}
