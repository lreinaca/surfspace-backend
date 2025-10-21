package com.eam.surfspace.persistence.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="pago")
public class PaymentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_pago")
    private Integer payId;

    @OneToOne
    @JoinColumn(name = "id_reserva", referencedColumnName = "id_reserva")
    private BookingEntity booking;
    
    @Enumerated(EnumType.STRING)
    @Column(name= "metodo_pago")
    private EnumPaymentMethod method;
     
    @Column(name= "monto")
    private Double amount;

    @Column(name= "fecha_pago")
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name= "estado")
    private EnumPaymentStatus status;

}