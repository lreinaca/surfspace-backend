package com.eam.surfspace.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "membresia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipEntity {
    @Id
    @Column(name = "id_membresia",nullable = false)
    private int idMembresia;
    
    @ManyToOne
    @Column(name = "id_usuario",nullable = false)
    private UserEntity idUsuario;

    @Column(name = "fecha_inicio", nullable = false)
    private Date fehaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private Date fehaFin;

    @Column(nullable = false)
    private String estado;
}
