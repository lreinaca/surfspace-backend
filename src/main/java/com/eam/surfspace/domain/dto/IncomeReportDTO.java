package com.eam.surfspace.domain.dto;

import com.eam.surfspace.persistence.entity.EnumPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class IncomeReportDTO {
    private LocalDate date;
    private Double totalIncome;
    private Long totalConfirmedBookings;
    private EnumPaymentMethod mostUsedPaymentMethod; //  TARJETA_DE_CREDITO, PSE
 }
