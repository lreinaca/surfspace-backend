package persistence;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="pago")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_pago")
    private int payId;

    @Column(name= "id_reserva")
    private int bookingId;
    
    @Enumerated(EnumType.STRING)
    @Column(name= "metodo_pago")
    private PaymentMethod paymentMethod;
     
    @Column(name= "monto")
    private Double amount;

    @Column(name= "fecha_pago")
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name= "estado")
    private PaymentStatus status;

}