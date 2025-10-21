package com.eam.surfspace.persistence.dao;

import com.eam.surfspace.domain.dto.MembershipCreateDTO;
import com.eam.surfspace.domain.dto.MembershipDTO;
import com.eam.surfspace.domain.dto.MembershipUpdateDTO;
import com.eam.surfspace.persistence.entity.MembershipEntity;
import com.eam.surfspace.persistence.entity.MembershipStatus;
import com.eam.surfspace.persistence.entity.UserEntity;
import com.eam.surfspace.persistence.mapper.MembershipMapper;
import com.eam.surfspace.persistence.repository.MembershipRepository;
import com.eam.surfspace.persistence.repository.UserRepository;
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
    private final UserRepository userRepository;

    private void expireMembershipsIfNeeded() {
        LocalDate now = LocalDate.now();
        List<MembershipEntity> expired = membershipRepository.findExpiredActive(now);
        for (MembershipEntity m : expired) {
            m.setEstado(MembershipStatus.INACTIVA);
            membershipRepository.save(m);
            UserEntity u = m.getIdUsuario();
            if (u != null) {
                userRepository.findById(u.getIdUsuario()).ifPresent(user -> {
                    user.setRol("VISITANTE");
                    userRepository.save(user);
                });
            }
        }
    }

    public MembershipDTO save(MembershipCreateDTO createDTO) {
        expireMembershipsIfNeeded();

        UserEntity user = userRepository.findById(createDTO.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));


        if (!user.getRol().equalsIgnoreCase("VISITANTE")) {
            throw new IllegalArgumentException("Solo los usuarios con rol VISITANTE pueden obtener membresía.");
        }

        boolean existsActive = membershipRepository.countByIdUsuario_IdUsuarioAndEstado(
                createDTO.getIdUsuario(), MembershipStatus.ACTIVA) > 0;

        if (existsActive) {
            throw new IllegalArgumentException("El usuario ya tiene una membresía activa.");
        }


        MembershipEntity entity = membershipMapper.toEntity(createDTO);
        entity.setIdUsuario(user);
        if (entity.getFechaInicio() == null) {
            entity.setFechaInicio(LocalDate.now());
        }
        entity.setFechaFin(entity.getFechaInicio().plusMonths(12));
        entity.setEstado(MembershipStatus.ACTIVA);

        MembershipEntity savedEntity = membershipRepository.save(entity);


        user.setRol("AFILIADO");
        userRepository.save(user);

        MembershipDTO dto = membershipMapper.toDTO(savedEntity);
        dto.setNombre(user.getNombre());

        return dto;
    }

    public Optional<MembershipDTO> findById(Integer id) {
        expireMembershipsIfNeeded();
        return membershipRepository.findById(id)
                .map(membershipMapper::toDTO);
    }

    public List<MembershipDTO> findAll() {
        expireMembershipsIfNeeded();
        List<MembershipEntity> entities = membershipRepository.findAll();
        return membershipMapper.toDTOList(entities);
    }

    public Optional<MembershipDTO> update(Integer id, MembershipUpdateDTO updateDTO) {
        expireMembershipsIfNeeded();
        return membershipRepository.findById(id)
                .map(existingEntity -> {
                    membershipMapper.updateEntityFromDTO(updateDTO, existingEntity);
                    if (existingEntity.getFechaFin() != null && existingEntity.getFechaFin().isBefore(LocalDate.now())) {
                        existingEntity.setEstado(MembershipStatus.INACTIVA);
                        UserEntity u = existingEntity.getIdUsuario();
                        if (u != null) {
                            userRepository.findById(u.getIdUsuario()).ifPresent(user -> {
                                user.setRol("VISITANTE");
                                userRepository.save(user);
                            });
                        }
                    } else {
                        existingEntity.setEstado(MembershipStatus.ACTIVA);
                        UserEntity u = existingEntity.getIdUsuario();
                        if (u != null) {
                            userRepository.findById(u.getIdUsuario()).ifPresent(user -> {
                                user.setRol("AFILIADO");
                                userRepository.save(user);
                            });
                        }
                    }
                    MembershipEntity updatedEntity = membershipRepository.save(existingEntity);
                    return membershipMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (membershipRepository.existsById(id)) {
            membershipRepository.findById(id).ifPresent(m -> {
                if (m.getEstado() == MembershipStatus.ACTIVA) {
                    UserEntity u = m.getIdUsuario();
                    if (u != null) {
                        userRepository.findById(u.getIdUsuario()).ifPresent(user -> {
                            user.setRol("VISITANTE");
                            userRepository.save(user);
                        });
                    }
                }
            });

            membershipRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public boolean isMembershipActive(Integer idUsuario) {
        expireMembershipsIfNeeded();
        return membershipRepository.countByIdUsuario_IdUsuarioAndEstado(idUsuario, MembershipStatus.ACTIVA) > 0;
    }

}
