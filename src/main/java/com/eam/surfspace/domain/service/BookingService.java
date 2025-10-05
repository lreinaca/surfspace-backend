package com.eam.surfspace.domain.service;

import com.eam.surfspace.domain.dto.BookingRequestDTO;
import com.eam.surfspace.domain.dto.BookingResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {


    BookingResponseDTO save(BookingRequestDTO bookingRequestDTO);

    List<BookingResponseDTO> getAllBookings();




}
