package com.eam.surfspace.web;

import com.eam.surfspace.domain.dto.IncomeReportDTO;
import com.eam.surfspace.domain.dto.OccupancyReportDTO;
import com.eam.surfspace.domain.dto.UserReportDTO;
import com.eam.surfspace.domain.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor

public class ReportController {
    private final BookingService.ReportService reportService;

    @GetMapping("/occupancy")
    @Operation(
            summary = "Obtener reporte de ocupación",
            description = "Devuelve un reporte de ocupación entre dos fechas",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de reportes de ocupación",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OccupancyReportDTO.class)))
            }
    )
    public ResponseEntity<List<OccupancyReportDTO>> getOccupancyReport(
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)", required = true,
                    schema = @Schema(type = "string", format = "date", example = "2025-10-20"))
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Fecha de fin (YYYY-MM-DD)", required = true,
                    schema = @Schema(type = "string", format = "date", example = "2025-10-27"))
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getOccupancyReport(startDate, endDate));
    }


    @GetMapping("/income")
    @Operation(
            summary = "Obtener reporte de ingresos",
            description = "Devuelve un reporte de ingresos basado en pagos confirmados entre dos fechas",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reporte de ingresos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncomeReportDTO.class)))
            }
    )
    public ResponseEntity<IncomeReportDTO> getIncomeReport(
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)", required = true,
                    schema = @Schema(type = "string", format = "date", example = "2025-01-01"))
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Fecha de fin (YYYY-MM-DD)", required = true,
                    schema = @Schema(type = "string", format = "date", example = "2025-01-31"))
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getIncomeReport(startDate, endDate));
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Obtener reporte de usuario",
            description = "Devuelve un reporte detallado de las actividades de un usuario específico",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Reporte de usuario",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserReportDTO.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado")
            }
    )
    public ResponseEntity<UserReportDTO> getUserReport(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer userId,
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)", required = true,
                    schema = @Schema(type = "string", format = "date", example = "2025-01-01"))
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Fecha de fin (YYYY-MM-DD)", required = true,
                    schema = @Schema(type = "string", format = "date", example = "2025-01-31"))
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getUserReport(userId, startDate, endDate));
    }

}
