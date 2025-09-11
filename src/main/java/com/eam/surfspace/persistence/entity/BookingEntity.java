package com.eam.surfspace.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Entity
@Table (name = "reserva")
@Data // CREA GETTERS Y SETTERS CON LIBRERIA LOMBOK
@NoArgsConstructor // CREA UN CONSTUCTOR VACIO
@AllArgsConstructor // CREA UN CONSTRUCTOR CON TODOS LOS ATRIBUTOS
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idBooking;

    @Column(name = "id_usuario", nullable = false)
    private int idUser;

    @Column(name = "id_espacio", nullable = false)
    private int idSpace;

    @Column(name = "fecha_reserva", nullable = false)
    private Date bookingDate;

    @Column(name = "hora_inicio", nullable = false)
    private Time startTime;

    @Column(name = "hora_fin", nullable = false)
    private Time endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "ENUM('PENDIENTE','CONFIRMADA','CANCELADA')")
    private EnumBookingStatus status;

    // @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    // private PagoEntity pago;




    
}
