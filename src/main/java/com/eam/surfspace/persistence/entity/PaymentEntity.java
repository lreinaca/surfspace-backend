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

    @Column(name= "id_reserva")
    private Integer bookingId;
    
    @Enumerated(EnumType.STRING)
    @Column(name= "metodo_pago")
    private EnumPaymentMethod paymentMethod;
     
    @Column(name= "monto")
    private Double amount;

    @Column(name= "fecha_pago")
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name= "estado")
    private EnumPaymentStatus status;

}