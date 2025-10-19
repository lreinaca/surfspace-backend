package com.eam.surfspace.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class NotificationDTO {
    private Integer notificationId;
    private Integer bookingId;
    private String type; // CONFIRMATION, REMINDER, PAYMENT_DUE
    private String message;
    private LocalDateTime sentAt;
    private String confirmationCode; // Solo para confirmaciones

}
