package com.eam.surfspace.domain.service;

import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.dto.NotificationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    NotificationDTO sendConfirmation(BookingResponseDTO bookingResponseDTO);
    NotificationDTO sendReminder(BookingResponseDTO bookingResponseDTO);
    NotificationDTO sendPaymentDue(Integer bookingId);
    List<NotificationDTO> getAllNotifications();
    String generateConfirmationCode(Integer bookingId);

}
