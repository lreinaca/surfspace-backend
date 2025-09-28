package com.eam.surfspace.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;


/**
 * Data Transfer Object for booking requests.
 * Contains the necessary information to create a new booking in the system.
 *
 * @Schema(description = "Information required to create a new booking")
 *
 * Example:
 * {
 *   "idMembership": 123,
 *   "idSpace": 1,
 *   "startDateTime": "2024-01-20T10:00:00",
 *   "endDateTime": "2024-01-20T11:00:00",
 *   "status": "PENDING"
 * }
 *
 * @property idMembership The membership identifier making the booking request
 * @property idSpace The space/room identifier being booked
 * @property startDateTime The start date and time of the booking
 * @property endDateTime The end date and time of the booking
 * @property status The status of the booking (validated and converted to ENUM in the Service)
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    @NotNull (message = "El id de la membresia es obligatorio")
    private Integer idMembership;

    @NotNull (message = "El id del espacio es obligatorio")
    private Integer idSpace;

    @NotNull (message = "La hora de inicio es obligatoria")
    private LocalDateTime startDateTime;

    @NotNull (message = "La hora de fin es obligatoria")
    private LocalDateTime endDateTime;

    @NotNull (message = "El estado es obligatorio")
    private String status; // en el Servicio se valida el estado de la reserva y se convierte a ENUM

}
