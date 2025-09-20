package com.eam.surfspace.domain.service;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.persistence.dao.BookingDAO;
import com.eam.surfspace.persistence.entity.BookingEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor

public class BookingServiceImpl implements BookingService {

   private final BookingDAO bookingDAO;

    // CREATE
    public BookingResponseDTO save(BookingRequestDTO bookingRequestDTO) {
        log.info("Create a new booking for a client in the coworking space");
        return bookingDAO.save(bookingRequestDTO);
    }
//
//    // FIND ALL
//    public List<BookingEntity> findAll() {
//        return bookingRepository.findAll();
//    }
//
//    // FIND BY ID
//    public Optional<BookingEntity> findById(Long id) {
//        return bookingRepository.findById(id);
//    }

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
