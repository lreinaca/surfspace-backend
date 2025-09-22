package com.eam.surfspace.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Data Transfer Object for booking responses.
 * Represents the information returned after a booking is created or retrieved.
 *
 * Example:
 * {
 *   "bookingId": 101,
 *   "idMembership": 123,
 *   "idSpace": 1,
 *   "startDateTime": "2024-01-20T10:00:00",
 *   "endDateTime": "2024-01-20T11:00:00",
 *   "status": "CONFIRMED"
 * }
 *
 * @property bookingId The unique identifier of the booking (auto-generated)
 * @property idMembership The membership identifier associated with the booking
 * @property idSpace The space/room identifier reserved
 * @property startDateTime The start date and time of the booking
 * @property endDateTime The end date and time of the booking
 * @property status The current status of the booking
 */

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookingResponseDTO {
    private Integer bookingId;
    private Integer idMembership;
    private Integer idSpace;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String status;

}
