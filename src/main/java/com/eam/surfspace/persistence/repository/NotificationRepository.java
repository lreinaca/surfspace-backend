package com.eam.surfspace.persistence.repository;

import com.eam.surfspace.persistence.entity.BookingEntity;
import com.eam.surfspace.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    // Heredamos los métodos CRUD básicos de JpaRepository

    // usamos JPQL para consultas personalizadas
    @Query("SELECT COUNT(b) > 0 FROM BookingEntity b WHERE b.confirmationCode = :code")
    boolean existsByBookingConfirmationCode(@Param("code") String code);

    @Query("SELECT COUNT(b) FROM BookingEntity b WHERE b.confirmationCode LIKE CONCAT(:prefix, '%')")
    long countByBookingConfirmationCodeStartingWith(@Param("prefix") String prefix);

    @Query("SELECT b FROM BookingEntity b WHERE b.startDateTime BETWEEN :start AND :end " +
            "AND NOT EXISTS (SELECT 1 FROM NotificationEntity n WHERE n.booking.id = b.id AND n.type = 'REMINDER')")
    List<BookingEntity> findBookingsNeedingReminder(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT b FROM BookingEntity b WHERE b.paymentStatus = 'PENDING' " +
            "AND b.createdAt < :cutoff " +
            "AND NOT EXISTS (SELECT 1 FROM NotificationEntity n WHERE n.booking.id = b.id AND n.type = 'PAGO')")
    List<BookingEntity> findBookingsWithPaymentDue(@Param("cutoff") LocalDateTime cutoff);
}
