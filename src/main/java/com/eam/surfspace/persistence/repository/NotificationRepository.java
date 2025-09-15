package com.eam.surfspace.persistence.repository;

import com.eam.surfspace.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    // Herredamos los métodos CRUD básicos de JpaRepository
}
