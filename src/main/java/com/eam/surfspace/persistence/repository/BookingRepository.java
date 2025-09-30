package com.eam.surfspace.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eam.surfspace.persistence.entity.BookingEntity;

import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
    // interfaz que extiende de JpaRepository para operaciones CRUD en BookingEntity

    // Métodos personalizados para reservas
    @Query("SELECT COUNT(b) > 0 FROM BookingEntity b " +
            "WHERE b.idSpace = :spaceId " +
            "AND b.status IN ('PENDIENTE', 'CONFIRMADA') " +
            "AND b.startDateTime < :endDateTime " +
            "AND b.endDateTime > :startDateTime")
    boolean existsBySpaceIdAndTimeOverlap(@Param("spaceId") Integer spaceId,
                                          @Param("startDateTime") LocalDateTime startDateTime,
                                          @Param("endDateTime") LocalDateTime endDateTime);
}

