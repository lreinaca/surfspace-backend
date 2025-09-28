package com.eam.surfspace.persistence.repository;

import com.eam.surfspace.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eam.surfspace.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {

    Optional<PaymentEntity> findByBooking_bookingId(Integer bookingId);

}
