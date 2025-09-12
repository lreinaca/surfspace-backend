package com.eam.surfspace.web;

import com.eam.surfspace.domain.service.NotificationService;
import com.eam.surfspace.persistence.entity.NotificationEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "API for Notification Management in the Coworking Space " +
        "Notifications are sent to clients regarding their bookings and other important updates.")

public class NotificationController {
    private final NotificationService notificationService;
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // SOLO EL LISTAR PORQUE DESDE EL FRONT NO VOY A REALIZAR OTRAS OPERACIONES
    // READ ALL
    @GetMapping
    @Operation(summary = "Get all notifications",
            description = "Retrieve a list of all notifications sent to clients in the coworking space")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of notifications retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No notifications found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
    })
    public ResponseEntity<NotificationEntity> getAllNotifications() {
        // TODO: implement logic
        return null;
    }




}
