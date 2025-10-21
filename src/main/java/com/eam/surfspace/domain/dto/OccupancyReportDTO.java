package com.eam.surfspace.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OccupancyReportDTO {
    private String spaceName;
    private Long totalHoursReserved;
    private Long totalBookings;
    private Double occupancyPercentage;

}
