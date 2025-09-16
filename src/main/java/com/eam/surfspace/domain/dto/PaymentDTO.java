package com.eam.surfspace.domain.dto;

import com.eam.surfspace.persistence.entity.BookingEntity;
import com.eam.surfspace.persistence.entity.EnumPaymentMethod;
import com.eam.surfspace.persistence.entity.EnumPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PaymentDTO {

    private Integer payId;
    private BookingEntity booking;
    private EnumPaymentMethod paymentMethod;
    private Double amount;
    private LocalDateTime paymentDate;
    private EnumPaymentStatus status;
}
