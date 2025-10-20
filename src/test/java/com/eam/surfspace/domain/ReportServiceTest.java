package com.eam.surfspace.domain;

import com.eam.surfspace.domain.dto.*;
import com.eam.surfspace.domain.service.impl.ReportServiceImpl;
import com.eam.surfspace.persistence.dao.BookingDAO;
import com.eam.surfspace.persistence.dao.PaymentDAO;
import com.eam.surfspace.persistence.entity.EnumPaymentMethod;
import com.eam.surfspace.persistence.entity.EnumPaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Report Service Test")
public class ReportServiceTest {

    @Mock
    private BookingDAO bookingDAO;

    @Mock
    private PaymentDAO paymentDAO;

    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    @DisplayName("OCCUPANCY - Debe generar reporte de ocupación correctamente")
    void testGetOccupancyReport_Success() {
        // Fechas del reporte
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);

        // Reservas dentro del rango para el espacio 1
        LocalDateTime b1Start = startDate.plusDays(0).atTime(9, 0);
        LocalDateTime b1End = b1Start.plusHours(2);
        BookingResponseDTO b1 = new BookingResponseDTO(1, 10, 1, b1Start, b1End, "CONFIRMADA");

        LocalDateTime b2Start = startDate.plusDays(1).atTime(14, 0);
        LocalDateTime b2End = b2Start.plusHours(3);
        BookingResponseDTO b2 = new BookingResponseDTO(2, 11, 1, b2Start, b2End, "CONFIRMADA");

        // Reserva fuera del rango (no debe contarse)
        LocalDateTime b3Start = startDate.minusDays(5).atTime(10, 0);
        LocalDateTime b3End = b3Start.plusHours(1);
        BookingResponseDTO b3 = new BookingResponseDTO(3, 12, 2, b3Start, b3End, "CONFIRMADA");

        when(bookingDAO.findAll()).thenReturn(Arrays.asList(b1, b2, b3));

        List<OccupancyReportDTO> report = reportService.getOccupancyReport(startDate, endDate);

        assertThat(report).isNotNull();
        // Debe haber un solo espacio con reservas (espacio "1")
        assertThat(report.size()).isEqualTo(1);

        OccupancyReportDTO dto = report.get(0);
        assertThat(dto.getSpaceName()).isEqualTo(String.valueOf(1));

        long expectedHours = Duration.between(b1Start, b1End).toHours() + Duration.between(b2Start, b2End).toHours();
        assertThat(dto.getTotalHoursReserved()).isEqualTo(expectedHours);
        assertThat(dto.getTotalBookings()).isEqualTo(2L);

        long totalAvailableHours = Duration.between(startDate.atStartOfDay(), endDate.atTime(23, 59)).toHours();
        double expectedOccupancy = (expectedHours * 100.0) / totalAvailableHours;
        assertThat(dto.getOccupancyPercentage()).isEqualTo(expectedOccupancy);
    }

    @Test
    @DisplayName("INCOME - Debe generar reporte de ingresos correctamente")
    void testGetIncomeReport_Success() {
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(5);

        // Pagos: 2 aprobados con TARJETA, 1 pendiente con PSE (no contado)
        BookingResponseDTO br1 = new BookingResponseDTO(1, 10, 1, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(2), "CONFIRMADA");
        PaymentDTO p1 = new PaymentDTO(1, br1, EnumPaymentMethod.TARJETA_DE_CREDITO, 100.0, LocalDateTime.now().minusDays(1), EnumPaymentStatus.APROBADO);

        BookingResponseDTO br2 = new BookingResponseDTO(2, 11, 2, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2).plusHours(1), "CONFIRMADA");
        PaymentDTO p2 = new PaymentDTO(2, br2, EnumPaymentMethod.TARJETA_DE_CREDITO, 50.0, LocalDateTime.now().minusDays(2), EnumPaymentStatus.APROBADO);

        BookingResponseDTO br3 = new BookingResponseDTO(3, 12, 3, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusHours(1), "CONFIRMADA");
        PaymentDTO p3 = new PaymentDTO(3, br3, EnumPaymentMethod.PSE, 30.0, LocalDateTime.now().minusDays(3), EnumPaymentStatus.PENDIENTE);

        when(paymentDAO.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

        IncomeReportDTO report = reportService.getIncomeReport(startDate, endDate);

        assertThat(report).isNotNull();
        double expectedTotal = 150.0;
        assertThat(report.getTotalIncome()).isEqualTo(expectedTotal);
        assertThat(report.getTotalConfirmedBookings()).isEqualTo(2L);
        assertThat(report.getMostUsedPaymentMethod()).isEqualTo(EnumPaymentMethod.TARJETA_DE_CREDITO);
    }

    @Test
    @DisplayName("USER - Debe generar reporte de usuario correctamente")
    void testGetUserReport_Success() {
        Integer userId = 10;
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(10);

        // Reservas del usuario dentro del rango
        BookingResponseDTO u1 = new BookingResponseDTO(1, userId, 1, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2).plusHours(2), "CONFIRMADA");
        BookingResponseDTO u2 = new BookingResponseDTO(2, userId, 2, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(3), "CONFIRMADA");
        BookingResponseDTO other = new BookingResponseDTO(3, 99, 1, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(1), "CONFIRMADA");

        when(bookingDAO.findAll()).thenReturn(Arrays.asList(u1, u2, other));

        // Pagos: uno aprobado correspondiente a u1, otro rechazado (no contado)
        PaymentDTO pay1 = new PaymentDTO(1, u1, EnumPaymentMethod.PSE, 80.0, LocalDateTime.now().minusDays(2), EnumPaymentStatus.APROBADO);
        PaymentDTO pay2 = new PaymentDTO(2, u2, EnumPaymentMethod.TARJETA_DE_CREDITO, 40.0, LocalDateTime.now().minusDays(1), EnumPaymentStatus.RECHAZADO);

        when(paymentDAO.findAll()).thenReturn(Arrays.asList(pay1, pay2));

        UserReportDTO report = reportService.getUserReport(userId, startDate, endDate);

        assertThat(report).isNotNull();
        assertThat(report.getUserId()).isEqualTo(userId);
        assertThat(report.getTotalBookings()).isEqualTo(2L);

        Map<String, Long> mostUsed = report.getMostUsedSpaces();
        // Esperamos que contenga los espacios "1" y "2" con conteos
        assertThat(mostUsed.containsKey(String.valueOf(1))).isTrue();
        assertThat(mostUsed.get(String.valueOf(1))).isEqualTo(1L);

        assertThat(report.getTotalPayments()).isEqualTo(80.0);
        assertThat(report.getMembershipStatus()).isEqualTo("ACTIVA");
    }
}

