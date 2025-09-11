package persistence;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id_usuario",nullable = false)
    private int idUsuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String rol;

    /*
    @OneToMany(mappedBy = "usuario")
    private List<MembresiaEntity> membresias;

    @OneToMany(mappedBy = "usuario")
    private List<ReservaEntity> reservas;

    @OneToMany(mappedBy = "usuario")
    private List<NotificacionEntity> notificaciones;
    */
}
