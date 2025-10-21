package com.eam.surfspace.persistence.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="espacio")
public class SpaceEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_espacio")
    private Integer idSpace;

    @Column(name= "nombre")
    private String name;
    
    @Column(name= "capacidad")
    private int capacity;
     
    @Column(name= "descripcion")
    private String description;

    @Enumerated(EnumType.STRING) //SALA_DE_REUNION, ESPACIO_COMPARTIDO
    @Column(name= "tipo")
    private EnumSpaceType type;

    @Enumerated(EnumType.STRING)
    @Column(name= "estado")
    private EnumSpaceStatus status;

}
