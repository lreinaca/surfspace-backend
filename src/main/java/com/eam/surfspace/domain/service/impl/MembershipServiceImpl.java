package com.eam.surfspace.domain.service.impl;
import com.eam.surfspace.domain.dto.MembershipCreateDTO;
import com.eam.surfspace.domain.dto.MembershipDTO;
import com.eam.surfspace.domain.dto.MembershipUpdateDTO;
import com.eam.surfspace.domain.service.MembershipService;
import com.eam.surfspace.persistence.dao.MembershipDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MembershipServiceImpl implements MembershipService {
    private final MembershipDAO membershipDAO;

    @Override
    public MembershipDTO save(MembershipCreateDTO createDTO) {
        log.info("Creating a new membership for user {}", createDTO.getIdUsuario());
        return membershipDAO.save(createDTO);
    }

    @Override
    public Optional<MembershipDTO> update(Integer id, MembershipUpdateDTO updateDTO) {
        log.info("Updating membership with id {}", id);
        return membershipDAO.update(id, updateDTO);
    }
    @Override
    public Optional<MembershipDTO> findById(Integer id) {
        log.info("Searching membership with id {}", id);
        return membershipDAO.findById(id);
    }

    @Override
    public List<MembershipDTO> findAll() {
        log.info("Listing all memberships");
        return membershipDAO.findAll();
    }
    @Override
    public boolean delete(Integer id) {
        log.info("Deleting membership with id {}", id);
        return membershipDAO.deleteById(id);
    }
    @Override
    public boolean isMembershipActive(Integer idUsuario) {
        log.info("Checking if user {} has an active membership", idUsuario);
        return membershipDAO.isMembershipActive(idUsuario);
    }
}
