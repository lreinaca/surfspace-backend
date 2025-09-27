package com.eam.surfspace.domain.service;

import com.eam.surfspace.domain.dto.PaymentDTO;
import com.eam.surfspace.persistence.entity.PaymentEntity;

import java.util.List;
//Aquí no está la lógica, solo dice qué métodos existen
public interface PaymentService {

    PaymentDTO savePayment(PaymentDTO paymentDTO);

    List<PaymentDTO> getAllPayment();

    PaymentDTO getPaymentByBooking(long idBooking);
}
