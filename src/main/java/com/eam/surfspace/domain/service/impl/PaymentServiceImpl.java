package com.eam.surfspace.domain.service.impl;

import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.dto.PaymentDTO;
import com.eam.surfspace.domain.service.NotificationService;
import com.eam.surfspace.domain.service.PaymentService;
import com.eam.surfspace.persistence.dao.BookingDAO;
import com.eam.surfspace.persistence.dao.PaymentDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional //si algo falla a mitad, se deshace cambios
@Slf4j// para los "log"
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentDAO paymentDAO;
    private final BookingDAO bookingDAO;
    private final NotificationService notificationService;

    //CREAR UN PAGO --------------------------------------------------------
    @Override
    public PaymentDTO savePayment(PaymentDTO paymentDTO) {
        // Validar datos del pago
        validatePaymentData(paymentDTO);

        log.info("Creating new payment for booking {}", paymentDTO.getBooking().getBookingId());

        // Validar que la reserva existe y está en estado PENDIENTE
        Integer bookingId = paymentDTO.getBooking().getBookingId();
        BookingResponseDTO booking = bookingDAO.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva con ID " + bookingId + " no encontrada"));

        if (!"PENDIENTE".equals(booking.getStatus())) {
            throw new IllegalArgumentException("Solo se pueden pagar reservas en estado PENDIENTE. Estado actual: " + booking.getStatus());
        }

        // Crear el pago
        PaymentDTO createdPayment = paymentDAO.save(paymentDTO);
        log.info("Payment with id {} created successfully", createdPayment.getPayId());

        // Cambiar estado de la reserva a CONFIRMADA
        bookingDAO.updateStatus(bookingId, "CONFIRMADA");
        log.info("Booking {} status updated to CONFIRMADA", bookingId);

        // Obtener la reserva actualizada para enviar notificación
        BookingResponseDTO confirmedBooking = bookingDAO.findById(bookingId).get();

        // Enviar notificación de confirmación
        notificationService.sendConfirmation(confirmedBooking);
        log.info("Confirmation notification sent for booking {}", bookingId);

        return createdPayment;
    }

    //OBTENER TODOS LOS PAGOS -------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<PaymentDTO> getAllPayment() {
        log.debug("getting all payments");
        return paymentDAO.findAll();
    }

    //OBTENER EL PAGO DE UNA RESERVA -------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentByIdBooking(Integer idBooking) {
        log.debug("getting payment by booking: {}", idBooking);

        return paymentDAO.findByBooking(idBooking).orElseThrow(() -> {
            log.warn("Producto no encontrado con ID: {}", idBooking);
            return new RuntimeException("Producto no encontrado con ID: " + idBooking);
        });
    }

    // Metodo de validación de datos--------------------------------------------
    private void validatePaymentData(PaymentDTO paymentDTO) {
        if (paymentDTO.getAmount() == null || paymentDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        if (paymentDTO.getMethod() == null) {
            throw new IllegalArgumentException("El método de pago no puede ser nulo o vacío");
        }
        if (paymentDTO.getDate() == null) {
            throw new IllegalArgumentException("La fecha de pago no puede ser nula");
        }
        if (paymentDTO.getStatus() == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo o vacío");
        }
        if (paymentDTO.getBooking() == null || paymentDTO.getBooking().getBookingId() == null) {
            throw new IllegalArgumentException("La reserva asociada a pago no puede ser nula");
        }
    }

}
