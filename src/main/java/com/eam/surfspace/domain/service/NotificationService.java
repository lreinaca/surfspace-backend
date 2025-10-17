package com.eam.surfspace.domain.service;

import com.eam.surfspace.domain.dto.NotificationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    NotificationDTO sendConfirmation(Integer bookingId);
    NotificationDTO sendReminder(Integer bookingId);
    NotificationDTO sendPaymentDue(Integer bookingId);
    List<NotificationDTO> getAllNotifications();
    String generateConfirmationCode();

}
