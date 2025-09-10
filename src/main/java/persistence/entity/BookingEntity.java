package persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data // CREA GETTERS Y SETTERS CON LIBRERIA LOMBOK
@NoArgsConstructor // CREA UN CONSTUCTOR VACIO
@AllArgsConstructor // CREA UN CONSTRUCTOR CON TODOS LOS ATRIBUTOS
public class BookingEntity {
    
    
}
