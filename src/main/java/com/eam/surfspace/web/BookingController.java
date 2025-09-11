package com.eam.surfspace.web;

import com.eam.surfspace.domain.service.BookingService;
import com.eam.surfspace.persistence.entity.BookingEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "API for Booking Management in the Coworking Space " +
        "Each client can have multiple bookings.")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // CREATE
    @PostMapping
    @Operation(summary = "Create a new booking",
            description = "Create a new booking for a client in the coworking space" +
                    "\n Each booking is associated with a client and includes details such as date, time, and workspace.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<BookingEntity> createBooking(@RequestBody BookingEntity booking) {
        // TODO: implement logic
        return null;
    }

    // READ ALL
    @GetMapping
    @Operation(summary = "Get all bookings",
            description = "Retrieve a list of all bookings in the coworking space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of bookings retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No bookings found")
    })
    public ResponseEntity<BookingEntity> getAllBookings() {
        // TODO: implement logic
        return null;
    }

    // READ BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID",
            description = "Retrieve a specific booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BookingEntity> getBookingById(
            @PathVariable
            @Parameter(description = "Booking identifier", required = true)
            Long id) {
        // TODO: implement logic
        return null;
    }

    // UPDATE
    @PutMapping("/{id}")
    @Operation(summary = "Update a booking",
            description = "Update the details of an existing booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BookingEntity> updateBooking(
            @PathVariable @Parameter(description = "Booking identifier", required = true)
            Long id,
            @RequestBody @Parameter(description = "Updated booking data", required = true)
            BookingEntity booking) {
            // TODO: implement logic
        return null;

    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a booking",
            description = "Delete an existing booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteBooking(
            @PathVariable @Parameter(description = "Booking identifier", required = true)
            Long id) {
        // TODO: implement logic
        return null;
    }


}
