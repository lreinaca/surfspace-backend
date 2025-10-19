package com.eam.surfspace.domain.service;

import com.eam.surfspace.domain.dto.MembershipCreateDTO;
import com.eam.surfspace.domain.dto.MembershipDTO;
import com.eam.surfspace.domain.dto.MembershipUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface MembershipService {

    MembershipDTO save(MembershipCreateDTO createDTO);

    Optional<MembershipDTO> update(Integer id, MembershipUpdateDTO updateDTO);

    Optional<MembershipDTO> findById(Integer id);

    List<MembershipDTO> findAll();
    boolean delete(Integer id);
    boolean isMembershipActive(Integer idUsuario);
}
