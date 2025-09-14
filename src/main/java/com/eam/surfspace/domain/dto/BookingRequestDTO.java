package com.eam.surfspace.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    @NotNull (message = "El id del usuario es obligatorio")
    private Integer idUser;

    @NotNull (message = "El id del espacio es obligatorio")
    private Integer idSpace;

    @NotNull(message = "La fecha es obligatoria")
    private Date bookingDate;

    @NotNull (message = "La hora de inicio es obligatoria")
    private Time startTime;

    @NotNull (message = "La hora de fin es obligatoria")
    private Time endTime;

    @NotNull (message = "El estado es obligatorio")
    private String status; // en el Servicio se valida el estado de la reserva y se convierte a ENUM

}
