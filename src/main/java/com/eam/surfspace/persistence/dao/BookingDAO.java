package com.eam.surfspace.persistence.dao;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.persistence.entity.BookingEntity;
import com.eam.surfspace.persistence.mapper.BookingMapper;
import com.eam.surfspace.persistence.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor

public class BookingDAO {
    // Lógica de acceso a datos personalizada
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingResponseDTO save(BookingRequestDTO bookingRequestDTO) {
        BookingEntity bookingToSave = bookingMapper.toBooking(bookingRequestDTO);
        BookingEntity bookingSaved =  bookingRepository.save(bookingToSave);
        return bookingMapper.toBookingDto(bookingSaved);
    }
}
