package com.eam.surfspace.domain.service.impl;

import com.eam.surfspace.domain.dto.*;
import com.eam.surfspace.domain.service.BookingService;
import com.eam.surfspace.persistence.dao.BookingDAO;
import com.eam.surfspace.persistence.dao.PaymentDAO;
import com.eam.surfspace.persistence.entity.EnumPaymentMethod;
import com.eam.surfspace.persistence.entity.EnumPaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements BookingService.ReportService {
    private final BookingDAO bookingDAO;
    private final PaymentDAO paymentDAO;

    public List<OccupancyReportDTO> getOccupancyReport(LocalDate startDate, LocalDate endDate) {
        log.info("Generating occupancy report from {} to {}", startDate, endDate);

        List<BookingResponseDTO> bookings = bookingDAO.findAll();
        Map<String, List<BookingResponseDTO>> bookingsBySpace = bookings.stream()
                .filter(b -> !b.getStartDateTime().toLocalDate().isBefore(startDate)
                        && !b.getEndDateTime().toLocalDate().isAfter(endDate))
                .filter(b -> !"CANCELADA".equals(b.getStatus()))
                .collect(Collectors.groupingBy(b -> String.valueOf(b.getIdSpace())));
        return bookingsBySpace.entrySet().stream()
                .map(entry -> {
                    String spaceName = entry.getKey();
                    List<BookingResponseDTO> spaceBookings = entry.getValue();

                    long totalHours = spaceBookings.stream()
                            .mapToLong(b -> Duration.between(b.getStartDateTime(), b.getEndDateTime()).toHours())
                            .sum();

                    long totalBookings = spaceBookings.size();
                    long totalAvailableHours = Duration.between(startDate.atStartOfDay(), endDate.atTime(23, 59)).toHours();
                    double occupancyPercentage = (totalHours * 100.0) / totalAvailableHours;

                    return new OccupancyReportDTO(spaceName, totalHours, totalBookings, occupancyPercentage);
                })
                .sorted(Comparator.comparing(OccupancyReportDTO::getOccupancyPercentage).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public IncomeReportDTO getIncomeReport(LocalDate startDate, LocalDate endDate) {
        log.info("Generating income report from {} to {}", startDate, endDate);

        List<PaymentDTO> payments = paymentDAO.findAll().stream()
                .filter(p -> !p.getDate().toLocalDate().isBefore(startDate)
                        && !p.getDate().toLocalDate().isAfter(endDate))
                .filter(p -> EnumPaymentStatus.APROBADO.equals(p.getStatus()))
                .collect(Collectors.toList());

        double totalIncome = payments.stream()
                .mapToDouble(PaymentDTO::getAmount)
                .sum();

        long totalConfirmedBookings = payments.size();

        EnumPaymentMethod mostUsedMethod = payments.stream()
                .collect(Collectors.groupingBy(PaymentDTO::getMethod, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return new IncomeReportDTO(startDate, totalIncome, totalConfirmedBookings, mostUsedMethod);
    }

    @Override
    public UserReportDTO getUserReport(Integer userId, LocalDate startDate, LocalDate endDate) {
        log.info("Generating user report for user {} from {} to {}", userId, startDate, endDate);

        List<BookingResponseDTO> userBookings = bookingDAO.findAll().stream()
                .filter(b -> userId.equals(b.getIdMembership()))
                .filter(b -> !b.getStartDateTime().toLocalDate().isBefore(startDate)
                        && !b.getEndDateTime().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());

        long totalBookings = userBookings.size();

        Map<String, Long> mostUsedSpaces = userBookings.stream()
                .collect(Collectors.groupingBy(b -> String.valueOf(b.getIdSpace()), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        Double totalPayments = paymentDAO.findAll().stream()
                .filter(p -> userBookings.stream()
                        .anyMatch(b -> b.getBookingId().equals(p.getBooking().getBookingId())))
                .filter(p -> EnumPaymentStatus.APROBADO.equals(p.getStatus()))
                .mapToDouble(PaymentDTO::getAmount)
                .sum();

        return new UserReportDTO(userId, "Usuario " + userId, totalBookings, mostUsedSpaces, totalPayments, "ACTIVA");
    }


}
