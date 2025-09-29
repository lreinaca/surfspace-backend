package com.eam.surfspace.persistence.dao;

import com.eam.surfspace.domain.dto.PaymentDTO;
import com.eam.surfspace.domain.dto.SpaceDTO;
import com.eam.surfspace.persistence.entity.EnumSpaceStatus;
import com.eam.surfspace.persistence.entity.PaymentEntity;
import com.eam.surfspace.persistence.entity.SpaceEntity;
import com.eam.surfspace.persistence.mapper.SpaceMapper;
import com.eam.surfspace.persistence.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpaceDAO {
    private final SpaceRepository spaceRepository;
    private final SpaceMapper spaceMapper;

    //CREAR UN ESPACIO --------------------------------------------------------------
    public SpaceDTO save(SpaceDTO spaceDTO){
        SpaceEntity entitySpaceToSave = spaceMapper.toSpaceEntity(spaceDTO);
        SpaceEntity spaceEntity = spaceRepository.save(entitySpaceToSave);
        return spaceMapper.toSpaceDTO(spaceEntity);
    }

    //OBTENER TODOS LOS PAGOS ------------------------------------------------------
    public List<SpaceDTO> findAll() {
        List<SpaceEntity> spaceEntities = spaceRepository.findAll();
        if (spaceEntities.isEmpty()) {
            return List.of(); //lista vacia
        }
        return spaceMapper.toSpaceDTOList(spaceEntities);
    }

    //OBTENER ESPACIO POR SU ID -----------------------------------------------------
    public Optional<SpaceDTO> findById(int idSpace) {
        return spaceRepository.findById(idSpace)
                .map(spaceMapper::toSpaceDTO);
    }

    //ACTUALIZAR INFO DE UN ESPACIO -------------------------------------------------
    public Optional<SpaceDTO> updateSpace(int idSpace, SpaceDTO updateDTO){
        return spaceRepository.findById(idSpace)
                .map(existingEntity -> {
                    spaceMapper.updateEntityFromDTO(updateDTO, existingEntity);
                    SpaceEntity updatedEntity = spaceRepository.save(existingEntity);
                    return spaceMapper.toSpaceDTO(updatedEntity);
                });
    }

    //DESACTIVAR UN ESPACIO ---------------------------------------------------------
    public void deactivateSpace(int idSpace) {
        spaceRepository.findById(idSpace).ifPresent(spaceEntity -> {
            spaceEntity.setStatus(EnumSpaceStatus.INACTIVO);
            spaceRepository.save(spaceEntity);
        });
    }
}
