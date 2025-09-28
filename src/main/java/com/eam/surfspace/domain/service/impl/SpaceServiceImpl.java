package com.eam.surfspace.domain.service.impl;

import com.eam.surfspace.domain.dto.BookingResponseDTO;
import com.eam.surfspace.domain.dto.SpaceDTO;
import com.eam.surfspace.domain.service.SpaceService;
import com.eam.surfspace.persistence.dao.SpaceDAO;
import com.eam.surfspace.persistence.entity.EnumSpaceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional //si algo falla a mitad, se deshace cambios
@Slf4j// para los "log"
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {
    private final SpaceDAO spaceDAO;

    //CREAR UN ESPACIO --------------------------------------------------------
    @Override //sirve para sobreescribir un metodo de una interfaz
    public SpaceDTO createSpace(SpaceDTO spaceDTO) {
        log.info("creating new space");

        //validar datos de ese espacio
        validateSpaceData(spaceDTO);

        //crear el espacio
        SpaceDTO createdSpace = spaceDAO.save(spaceDTO);
        log.info("space with id: {} created successfully", createdSpace.getIdSpace());
        return /*createdSpace*/null;
    }

    //OBTENER TODOS LOS ESPACIOS -------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<SpaceDTO> getAllSpaces() {
        log.debug("getting all spaces");
        return spaceDAO.findAll();
    }

    //OBTENER EL ESPACIO POR ID -------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public SpaceDTO getSpaceById(Integer idSpace) {
        log.debug("getting space by id: {}", idSpace);

        return spaceDAO.findById(idSpace).orElseThrow(() -> {
            log.warn("Space not found with ID: {}", idSpace);
            return new RuntimeException("Space not found with ID: " + idSpace);
        });
    }

    //ACTUALIZAR ESPACIO ------------------------------------------------
    @Override
    public SpaceDTO updateSpace(Integer idSpace, SpaceDTO spaceDTO) {
        log.info("updating space with id: {}", idSpace);

        //validar datos del espacio
        validateSpaceData(spaceDTO);

        //buscar el espacio a actualizar
        SpaceDTO existingSpace = getSpaceById(idSpace);

        //actualizar los campos necesarios
        existingSpace.setName(spaceDTO.getName());
        existingSpace.setCapacity(spaceDTO.getCapacity());
        existingSpace.setDescription(spaceDTO.getDescription());
        existingSpace.setType(spaceDTO.getType());
        existingSpace.setStatus(spaceDTO.getStatus());

        //guardar los cambios
        SpaceDTO updatedSpace = spaceDAO.updateSpace(idSpace, spaceDTO);
        log.info("space with id: {} updated successfully", updatedSpace.getIdSpace());
        return updatedSpace;
    }

    //DESACTIVAR ESPACIO ------------------------------------------------
    @Override
    public void deactivateSpace(Integer idSpace) {
        log.info("deactivating space with id: {}", idSpace);

        //buscar el espacio a desactivar
        SpaceDTO existingSpace = getSpaceById(idSpace);

        //cambiar el estado a IDLE
        existingSpace.setStatus(EnumSpaceStatus.IDLE);

        //guardar los cambios
        spaceDAO.save(existingSpace);
        log.info("space with id: {} deactivated successfully", idSpace);
    }

    //Metodo de validación de datos--------------------------------------------
    public void validateSpaceData(SpaceDTO spaceDTO){
        if (spaceDTO.getIdSpace() == null || spaceDTO.getIdSpace() <= 0){
            throw new IllegalArgumentException("El id no puede ser nulo o un número negativo");
        }
        if (spaceDTO.getStatus() == null){
            throw new IllegalArgumentException("Se debe definir el estado del espacio, no debe ser nulo");
        }
        if (spaceDTO.getType() == null){
            throw new IllegalArgumentException("Se debe definir el tipo de espacio, no puede ser nulo");
        }
        if (spaceDTO.getName() == null){
            throw new IllegalArgumentException("El espacio debe tener un nombre, no puede ser nulo");
        }
        if (spaceDTO.getDescription() == null){
            throw new IllegalArgumentException("El espacio debe tener una descripción, no puede ser nula");
        }
        if (spaceDTO.getCapacity() <= 1){
            throw new IllegalArgumentException("La capacidad debe ser un número positivo");
        }
    }

    //Validar si un espacio está activo ---------------------------------------
    public boolean isSpaceAvailable(int idSpace, LocalDateTime requestedStart, LocalDateTime requestedEnd){
        //verificar existencia del espacio que recibimos
        SpaceDTO space = spaceDAO.findById(idSpace)
                .orElseThrow(()-> new IllegalArgumentException("The space not found"));

        //verificamos su estado
        if (space.getStatus() != EnumSpaceStatus.AVAILABLE){
            return false;
        }

        //verificar que la nueva reserva no choque en tiempo con una ya existente
        List<BookingResponseDTO> overLappingBookings = bookingServiceImpl;

        return true; //el espacio está activo y disponible
    }

}

