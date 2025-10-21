package com.eam.surfspace.domain.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReportDTO {
    private Integer userId;
    private String userName;
    private Long totalBookings;
    private Map<String, Long> mostUsedSpaces; // Nombre del espacio y número de reservas
    private Double totalPayments;
    private String membershipStatus; // ACTIVO, INACTIVO
}
