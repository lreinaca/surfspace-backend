package com.eam.surfspace.persistence.dao;

import com.eam.surfspace.domain.dto.PaymentDTO;
import com.eam.surfspace.persistence.entity.PaymentEntity;
import com.eam.surfspace.persistence.mapper.PaymentMapper;
import com.eam.surfspace.persistence.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentDAO {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    //CREAR UN PAGO ----------------------------------------------------------------
    public PaymentDTO save(PaymentDTO paymentDTO){
        PaymentEntity EntityPaymentToSave = paymentMapper.toPaymentEntity(paymentDTO);
        PaymentEntity paymentEntity = paymentRepository.save(EntityPaymentToSave);
        return paymentMapper.toPaymentDTO(paymentEntity);
    }

    //OBTENER TODOS LOS PAGOS ------------------------------------------------------
    public List<PaymentDTO> findAll(){
        List<PaymentEntity> payEntities = paymentRepository.findAll();
        return paymentMapper.toPaymentDTOList(payEntities);
    }

    //OBTENER EL PAGO DE UNA RESERVA ------------------------------------------------
    public Optional<PaymentDTO> findByBooking(long idBooking){ //opcional para evitar que retorne null
        return paymentRepository.findByBooking(idBooking).map(paymentMapper::toPaymentDTO);
    }
}
