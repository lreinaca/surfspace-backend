package com.eam.surfspace.persistence.dao;

import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.dto.NotificationDTO;
import com.eam.surfspace.persistence.entity.BookingEntity;
import com.eam.surfspace.persistence.entity.NotificationEntity;
import com.eam.surfspace.persistence.mapper.BookingMapper;
import com.eam.surfspace.persistence.mapper.NotificationMapper;
import com.eam.surfspace.persistence.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationDAO {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final BookingMapper bookingMapper;

    public NotificationDTO save(NotificationDTO notificationDTO) {
        NotificationEntity entity = notificationMapper.toEntity(notificationDTO);
        NotificationEntity saved = notificationRepository.save(entity);
        return notificationMapper.toDto(saved);
    }

    public List<NotificationDTO> findAll() {
        List<NotificationEntity> entities = notificationRepository.findAll();
        return notificationMapper.toDtoList(entities);
    }

    public boolean existsByConfirmationCode(String code) {
        return notificationRepository.existsByBookingConfirmationCode(code);
    }

    public long countByConfirmationCodeStartingWith(String prefix) {
        return notificationRepository.countByBookingConfirmationCodeStartingWith(prefix);
    }

    // Para los recordatorios de reservas 24 horas antes de la reserva
    // targetTime es "ahora", buscamos reservas que comiencen dentro de aproximadamente
    // 24 horas (entre 23 y 25 horas desde ahora).
    // Esto da un margen para enviar el recordatorio.
    public List<BookingResponseDTO> findBookingsNeedingReminder(LocalDateTime targetTime) {
        LocalDateTime start = targetTime.plusHours(23);
        LocalDateTime end = targetTime.plusHours(25);
        List<BookingEntity> entities = notificationRepository.findBookingsNeedingReminder(start, end);
        return bookingMapper.toBookingsDto(entities);
    }
    // Para los avisos de vencimiento de pago una hora antes del vencimiento
    // cutoffTime es "ahora", buscamos reservas creadas hace más de 1 hora
    // que aún están pendientes de pago.
    public List<BookingResponseDTO> findBookingsWithPaymentDue(LocalDateTime cutoffTime) {
        LocalDateTime cutoff = cutoffTime.minusHours(1);
        List<BookingEntity> entities = notificationRepository.findBookingsWithPaymentDue(cutoff);
        return bookingMapper.toBookingsDto(entities);
    }

}
