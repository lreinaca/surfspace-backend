package com.eam.surfspace.domain.service.impl;

import com.eam.surfspace.domain.dto.PaymentDTO;
import com.eam.surfspace.domain.service.PaymentService;
import com.eam.surfspace.persistence.dao.PaymentDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional //si algo falla a mitad, se deshace cambios
@Slf4j //para los "log"
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private PaymentDAO paymentDAO;

    //CREAR UN PAGO --------------------------------------------------------
    @Override
    public PaymentDTO savePayment(PaymentDTO paymentDTO) {
        log.info("creating new payment");

        //validar datos de ese pago
        validatePaymentData(paymentDTO);

        //crear el pago
        PaymentDTO createdPayment = paymentDAO.save(paymentDTO);
        log.info("payment created successfully");
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
    public PaymentDTO getPaymentByBooking(long idBooking) {
        log.debug("getting payment by booking: {}", idBooking);

        return paymentDAO.findByBooking(idBooking).orElseThrow(() -> {
            log.warn("Producto no encontrado con ID: {}", idBooking);
            return new RuntimeException("Producto no encontrado con ID: " + idBooking);
        });
    }

    //POSIBLE METODO PARA TODOS LOS PAGOS HECHOS POR UNA PERSONA, PARA REPORTES

    // Metodo de validación de datos
    private void validatePaymentData(PaymentDTO paymentDTO) {
        //TO DO
    }

}
