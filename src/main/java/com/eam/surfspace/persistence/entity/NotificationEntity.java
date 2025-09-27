package com.eam.surfspace.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
@Data // CREA GETTERS Y SETTERS CON LIBRERIA LOMBOK
@NoArgsConstructor // CREA UN CONSTUCTOR VACIO
@AllArgsConstructor // CREA UN CONSTRUCTOR CON TODOS LOS ATRIBUTOS

public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long idNotification;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario",
            foreignKey = @ForeignKey(name = "fk_notificacion_usuario"))
    private UserEntity usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, columnDefinition =
            "ENUM('SISTEMA','RESERVA','PAGO','PROMOCION','OTRO')")
    private EnumNotificationType type;

    @Column(name = "mensaje", nullable = false, length = 500)
    private String message;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime sentDate;

    @ManyToOne
    @JoinColumn(name = "id_reserva", referencedColumnName = "id_reserva",
            foreignKey = @ForeignKey(name = "fk_notificacion_reserva"))
    private BookingEntity booking;

    @ManyToOne
    @JoinColumn(name = "id_pago", referencedColumnName = "id_pago",
            foreignKey = @ForeignKey(name = "fk_notificacion_pago"))
    private PaymentEntity pago;

}
