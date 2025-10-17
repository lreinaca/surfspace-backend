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
    @Query("SELECT COUNT(b) > 0 FROM BookingEntity b WHERE b.bookingId = :bookingId")
    boolean existsByBookingConfirmationCode(@Param("bookingId") Integer bookingId);

    @Query("SELECT COUNT(b) FROM BookingEntity b WHERE b.bookingId = :bookingId")
    long countByBookingConfirmationCodeStartingWith(@Param("prefix") Integer bookingId);

    @Query("SELECT b FROM BookingEntity b WHERE b.startDateTime BETWEEN :start AND :end " +
            "AND NOT EXISTS (SELECT 1 FROM NotificationEntity n WHERE n.booking.bookingId = b.bookingId AND n.type = 'REMINDER')")
    List<BookingEntity> findBookingsNeedingReminder(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT DISTINCT b FROM BookingEntity b " +
            "JOIN PaymentEntity p ON p.booking.bookingId = b.bookingId " +
            "WHERE p.status = 'PENDIENTE' " +
            // "AND p.paymentDeadline < :cutoff " +
            "AND NOT EXISTS (SELECT 1 FROM NotificationEntity n " +
            "WHERE n.booking.bookingId = b.bookingId AND n.type = 'PAYMENT_DUE')")
    List<BookingEntity> findBookingsWithPaymentDue(@Param("cutoff") LocalDateTime cutoff);

}
