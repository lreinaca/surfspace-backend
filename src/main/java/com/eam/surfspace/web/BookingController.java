package com.eam.surfspace.web;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * BookingController
 * Controller for managing bookings in the coworking space
 * Each client can have multiple bookings.
 */
@RestController // recibe y contesta peticiones de tipo rest http
@RequestMapping("/api/bookings") // para acceder al controlador desde una ruta http
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bookings", description = "API for Booking Management in the Coworking Space " +
        "Each client can have multiple bookings.")
@CrossOrigin(origins = "*") // sino pongo esto no puede recibir ninguna petición , no sabe a quien escuchar , se cambia cuando empecemos el frontend , ponemos aca la url

public class BookingController {

    private final BookingService bookingService;

    // CREATE
    /***
     * Create a new booking
     * Create a new booking for a client in the coworking space
     * Each booking is associated with a client and includes details such as date, time, and workspace.
     * @param booking Booking data
     * @return Created booking
     */
    @PostMapping
    @Operation(summary = "Create a new booking",
            description = "Create a new booking for a client in the coworking space" +
                    "\n Each booking is associated with a client and includes details such as date, time, and workspace.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    // siempre retorno DTO RESPONSE Y RECIBO UN DTO REQUEST
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Parameter(description = "Booking data", required = true)
            @RequestBody BookingRequestDTO booking) {
           log.info("POST api/bookings - Creating new booking, for Membership: {}", booking.getIdMembership());
        try {
            BookingResponseDTO bookingSaved = bookingService.save(booking);
            log.info("Booking created successfully: {}", bookingSaved.getBookingId());
            return ResponseEntity.status(HttpStatus.CREATED).body(bookingSaved);
        } catch (IllegalArgumentException e) {
            log.warn("Error creating booking: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Internal server error while creating booking: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /***
     * READ ALL
     * Get all bookings
     * Retrieve a list of all bookings in the coworking space
     * @return List of bookings
     */
    @GetMapping
    @Operation(summary = "Get all bookings",
            description = "Retrieve a list of all bookings in the coworking space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of bookings retrieved successfully"),
    })
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        log.debug("GET /api/bookings - Retrieving all bookings");

        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        log.debug("Se encontraron {} bookings", bookings.size());
        return ResponseEntity.ok(bookings);
    }

    // READ BY ID
    /***
     * Get booking by ID
     * Retrieve a specific booking by its ID
     * @param id Booking identifier
     * @return Booking details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID",
            description = "Retrieve a specific booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
    })
    public ResponseEntity<BookingResponseDTO> getBookingById(
            @PathVariable
            @Parameter(description = "Booking identifier", required = true, example = "1")
            Integer id) {
            log.debug("GET /api/bookings by ID {}", id);
            try {
                BookingResponseDTO booking = bookingService.getBookingById(id);
                    log.debug("Booking found: {}", booking);
                    return ResponseEntity.ok(booking);
                } catch (RuntimeException e) {
                log.warn("Booking with ID {} not found", id);
                return ResponseEntity.notFound().build();
            }
    }

    // UPDATE
    @PutMapping("/{id}")
    @Operation(summary = "Update a booking",
            description = "Update the details of an existing booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<BookingResponseDTO> updateBooking(
            @PathVariable @Parameter(description = "Booking identifier", required = true)
            Integer id,
            @RequestBody @Parameter(description = "Updated booking data", required = true)
            BookingRequestDTO booking) {
            log.info("PUT /api/bookings/{} - Actualizando reserva", id);
            try {
                BookingResponseDTO updatedBooking = bookingService.update(id, booking);
                log.info("Booking updated: {}", updatedBooking);
                return  ResponseEntity.ok(updatedBooking);
            } catch (RuntimeException e) {
                if (e.getMessage().contains("not found")) {
                    log.warn("Booking with ID {} not found: {}", id, e.getMessage());
                    return ResponseEntity.notFound().build();
                }
                log.warn("Error updating booking: {}", e.getMessage());
                return ResponseEntity.badRequest().build();
            }

    }

    // NO se eliminan reservas, solo se les cambia el estado
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel a booking",
            description = "Change booking status from CONFIRMADA to CANCELADA (no se elimina el recurso)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied or business rule violation"),
    })
    public ResponseEntity<Void> cancelBooking(
            @PathVariable @Parameter(description = "Booking identifier", required = true)
            Integer id) {
             log.info("PATCH /api/bookings/{} - Cancelling booking", id);
             try {
                 bookingService.cancelBooking(id);
                    log.info("Booking with ID {} deleted successfully", id);
                    return ResponseEntity.noContent().build();
             }catch (RuntimeException e){
                   if (e.getMessage().contains("not found")) {
                          log.warn("Booking with ID {} not found: {}", id, e.getMessage());
                          return ResponseEntity.notFound().build();
                   }
                   log.warn("Error updating booking: {}", e.getMessage());
                     return ResponseEntity.badRequest().build();
             }

    }


}
