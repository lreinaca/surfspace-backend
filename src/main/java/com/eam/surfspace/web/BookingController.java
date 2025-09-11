package com.eam.surfspace.web;

import com.eam.surfspace.domain.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eam.surfspace.persistence.entity.BookingEntity;

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
    @Operation(summary = "Create a new booking", description = "Create a new booking for a client in the coworking space" +
            " Each booking is associated with a client and includes details such as date, time, and workspace.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<BookingEntity> createBooking(@RequestBody BookingEntity booking) {
        // TODO: implement logic
        return null;
    }


}
