package com.eam.surfspace.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.eam.surfspace.persistence.entity.BookingEntity;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    // interfaz que extiende de JpaRepository para operaciones CRUD en BookingEntity

}
