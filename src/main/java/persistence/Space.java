package persistence;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="espacio")
public class Space {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_espacio")
    private int idSpace;

    @Column(name= "nombre")
    private String name;
    
    @Column(name= "capacidad")
    private int capacity;
     
    @Column(name= "descripcion")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name= "tipo")
    private SpaceType type;

    @Enumerated(EnumType.STRING)
    @Column(name= "estado")
    private SpaceStatus status;

}
