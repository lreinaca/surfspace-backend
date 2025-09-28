package com.eam.surfspace.domain.dto;

import com.eam.surfspace.persistence.entity.BookingEntity;
import com.eam.surfspace.persistence.entity.EnumPaymentMethod;
import com.eam.surfspace.persistence.entity.EnumPaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Managing payments for a reservation")

//PaymentDTO es para respuestas

public class PaymentDTO {

    @Schema(description = "Unique ID of a payment", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer payId;

    @Schema(description = "Reservation related to payment", required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private BookingResponseDTO booking;

    @Schema(description = "Payment method", required = true)
    private EnumPaymentMethod method;

    @Schema(description = "Amount to pay", example = "150000.0", required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Double amount;

    @Schema(description = "Date and time the payment is made", example = "2025-09-07T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime date;

    @Schema(description = "Status of a payment", required = true)
    private EnumPaymentStatus status;
}
