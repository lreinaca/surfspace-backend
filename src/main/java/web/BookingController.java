package web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "CarritoCompras", description = "API para la gestión de Reservas en el cooworking. " +
        "Cada Cliente puede tener varias reservas.")
public class BookingController {


}
