package com.eam.surfspace.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookigResponseDTO {
    private Long idBookig;
    private Integer idUser;
    private Integer idSpace;
    private Date bookingDate;
    private Time startTime;
    private Time endTime;
    private String status;

}
