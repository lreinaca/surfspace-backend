package com.eam.surfspace.persistence.dao;

import com.eam.surfspace.domain.dto.MembershipCreateDTO;
import com.eam.surfspace.domain.dto.MembershipDTO;
import com.eam.surfspace.domain.dto.MembershipUpdateDTO;
import com.eam.surfspace.persistence.entity.MembershipEntity;
import com.eam.surfspace.persistence.entity.MembershipStatus;
import com.eam.surfspace.persistence.mapper.MembershipMapper;
import com.eam.surfspace.persistence.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class MembershipDAO {
    private final MembershipRepository membershipRepository;
    private final MembershipMapper membershipMapper;


    public MembershipDTO save(MembershipCreateDTO createDTO) {
        boolean existsActive = membershipRepository.countByIdUsuario_IdUsuarioAndEstado(
                createDTO.getIdUsuario(), MembershipStatus.ACTIVA) > 0;

        if (existsActive) {
            throw new IllegalArgumentException("El usuario ya tiene una membresía activa.");
        }

        MembershipEntity entity = membershipMapper.toEntity(createDTO);
        MembershipEntity savedEntity = membershipRepository.save(entity);
        return membershipMapper.toDTO(savedEntity);
    }


    public Optional<MembershipDTO> findById(Integer id) {
        return membershipRepository.findById(id)
                .map(membershipMapper::toDTO);
    }


    public List<MembershipDTO> findAll() {
        List<MembershipEntity> entities = membershipRepository.findAll();
        return membershipMapper.toDTOList(entities);
    }


    public Optional<MembershipDTO> update(Integer id, MembershipUpdateDTO updateDTO) {
        return membershipRepository.findById(id)
                .map(existingEntity -> {
                    membershipMapper.updateEntityFromDTO(updateDTO, existingEntity);
                    MembershipEntity updatedEntity = membershipRepository.save(existingEntity);
                    return membershipMapper.toDTO(updatedEntity);
                });
    }


    public boolean deleteById(Integer id) {
        if (membershipRepository.existsById(id)) {
            membershipRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
