package com.eam.surfspace.persistence.dao;

import com.eam.surfspace.domain.dto.PaymentDTO;
import com.eam.surfspace.persistence.entity.PaymentEntity;
import com.eam.surfspace.persistence.mapper.PaymentMapper;
import com.eam.surfspace.persistence.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PaymentDAO {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    //CREAR UN PAGO ----------------------------------------------------------------
    public PaymentDTO save(PaymentDTO paymentDTO){
        PaymentEntity entityPaymentToSave = paymentMapper.toPaymentEntity(paymentDTO);

        log.info("Amount recibido en entidad: {}", entityPaymentToSave.getAmount());
        if(entityPaymentToSave.getAmount() == null) {
            throw new IllegalArgumentException("the mount to pay cannot be null");
        }
        PaymentEntity paymentEntity = paymentRepository.save(entityPaymentToSave);
        return paymentMapper.toPaymentDTO(paymentEntity);
    }

    //OBTENER TODOS LOS PAGOS ------------------------------------------------------
    public List<PaymentDTO> findAll() {
        List<PaymentEntity> payEntities = paymentRepository.findAll();
        if (payEntities.isEmpty()) {
            return List.of();
        }
        return paymentMapper.toPaymentDTOList(payEntities);
    }

    //OBTENER EL PAGO DE UNA RESERVA ------------------------------------------------
    public Optional<PaymentDTO> findByBooking(Integer idBooking){ //opcional para evitar que retorne null
        return paymentRepository.findByBooking_bookingId(idBooking).map(paymentMapper::toPaymentDTO);
    }
}
