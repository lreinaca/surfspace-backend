package com.eam.surfspace.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table (name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer bookingId;

    @Column(name = "id_membresia", nullable = false)
    private int idMembership;

    @Column(name = "id_espacio", nullable = false)
    private int idSpace;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "ENUM('PENDIENTE','CONFIRMADA','CANCELADA','EXPIRADA')")
    private EnumBookingStatus status;

    /*
    En esta entidad (Reserva) existe una relación uno a uno con PaymentEntity,
     y esa relación está controlada desde el atributo booking en PaymentEntity;
     además, cuando cargue la reserva no traeré el pago hasta que se necesite (lazy),
     y cualquier operación de guardar/actualizar/borrar sobre la reserva también se aplicará a su pago,
      eliminándolo si queda huérfano.
     */
    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentEntity pago;

}
