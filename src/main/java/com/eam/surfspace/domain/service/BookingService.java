package com.eam.surfspace.domain.service;

import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.persistence.entity.BookingEntity;
import com.eam.surfspace.persistence.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    //  TODO  implementar el uso de DTO y Mappers
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // CREATE
    public BookingEntity save(BookingEntity bookingEntity) {
        return bookingRepository.save(bookingEntity);
    }

    // FIND ALL
    public List<BookingEntity> findAll() {
        return bookingRepository.findAll();
    }

    // FIND BY ID
    public Optional<BookingEntity> findById(Long id) {
        return bookingRepository.findById(id);
    }

    // metodo para validar reglas de negocio respecto a las horas
    private void validateTimes(Time start,  Time end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Hora de Inicio y Hora de Fin son Obligatorias");
        }
        if (!end.before(start)) {
            throw new IllegalArgumentException("Hora Fin debe ser mayor a la hora de inicio");
        }

        // TODO incluir validaciones adicionales
        //  if(Time.now )
    }

}
