package com.eam.surfspace.domain.service;

import com.eam.surfspace.domain.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface BookingService {

    /**
     * Create a new booking
     * Create a new booking for a client in the coworking space
     * Each booking is associated with a client and includes details such as date, time, and workspace.
     * @param bookingRequestDTO Booking data
     * @return Created booking
     */
    BookingResponseDTO save(BookingRequestDTO bookingRequestDTO);

    /**
     * Update an existing booking
     * Update the details of an existing booking by its ID.
     * @param id Booking ID
     * @param bookingRequestDTO Updated booking data
     * @return Updated booking
     */
    BookingResponseDTO update(Integer id, BookingRequestDTO bookingRequestDTO);

    /**
     * Delete a booking by ID
     * Remove a specific booking from the system using its ID.
     * @param id Booking ID
     */
    void cancelBooking(Integer id);

    /**
     * Get all bookings
     * Retrieve a list of all bookings in the coworking space
     * @return List of bookings
     */
    List<BookingResponseDTO> getAllBookings();

    /**
     * Get booking by ID
     * Retrieve a specific booking by its ID
     * @param id Booking identifier
     * @return Booking details
     */
    BookingResponseDTO getBookingById(Integer id);


    interface ReportService {
        List<OccupancyReportDTO> getOccupancyReport(LocalDate startDate, LocalDate endDate);

        IncomeReportDTO getIncomeReport(LocalDate startDate, LocalDate endDate);

        UserReportDTO getUserReport(Integer userId, LocalDate startDate, LocalDate endDate);
    }
}
