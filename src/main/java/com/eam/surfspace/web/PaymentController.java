package com.eam.surfspace.web;

import com.eam.surfspace.domain.dto.PaymentDTO;
import com.eam.surfspace.domain.service.PaymentService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.eam.surfspace.persistence.entity.PaymentEntity;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") //colocar url del frontend
@Tag(name = "Payments", description = "API for managing payments in the system")
public class PaymentController  {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;


    // CREATE PAYMENT--------------------------------------
    @PostMapping
    @Operation(summary = "Create a new payment", description = "Creates a new payment in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaymentDTO> createPayment(
            @Parameter(description = "Datos del pago a crear", required = true)
            @RequestBody PaymentDTO paymentDTO) {
        log.info("POST /api/payments - Creating payment for booking");

        try {
            PaymentDTO newPayment = paymentService.savePayment(paymentDTO);
            log.info("payment created successfully\n");
            return ResponseEntity.status(HttpStatus.CREATED).body(newPayment);
        } catch (IllegalArgumentException e) {
            log.warn("Error creating payment", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // READ ALL---------------------------------------------------
    @GetMapping
    @Operation(summary = "Get all payments", description = "Retrieves all payments in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<PaymentDTO>> getAllPayments(){
        log.debug("GET /api/payments - Getting all the payments");

        List<PaymentDTO> payments = paymentService.getAllPayment();
        log.debug("{} payments were found", payments.size());
        return ResponseEntity.ok(payments);
    }

    //READ PAYMENT FOR ID BOOKING---------------------------------
    @GetMapping("/booking/{idBooking}")
    @Operation(summary = "Get payment for a booking", description = "get payment per reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<PaymentDTO> getPaymentByBooking(
            @Parameter(description = "booking ID", required = true)
            @PathVariable long idBooking
    ){
        log.debug("GET /api/payments/booking/{} - payment for booking", idBooking);

        try {
            PaymentDTO payment = paymentService.getPaymentByBooking(idBooking);
            log.debug("Payment for booking {} found", idBooking);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            log.warn("Payment for this booking -{}-has not yet been made or was not found", idBooking);
            return ResponseEntity.notFound().build();
        }
    }

}