package com.eam.surfspace.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eam.surfspace.persistence.entity.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {

}
