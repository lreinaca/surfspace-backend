package com.eam.surfspace.domain;
import com.eam.surfspace.domain.dto.MembershipCreateDTO;
import com.eam.surfspace.domain.dto.MembershipDTO;
import com.eam.surfspace.domain.dto.MembershipUpdateDTO;
import com.eam.surfspace.domain.service.MembershipService;
import com.eam.surfspace.domain.service.impl.MembershipServiceImpl;
import com.eam.surfspace.persistence.dao.MembershipDAO;

import com.eam.surfspace.persistence.entity.MembershipStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Membership Service Test")
public class MembershipServiceTest {
    @Mock
    private MembershipDAO membershipDAO;

    @Mock
    private MembershipService membershipService;

    @InjectMocks
    private MembershipServiceImpl membershipServiceImpl;

    private MembershipCreateDTO createDTO;
    private MembershipUpdateDTO updateDTO;
    private MembershipDTO membershipDTO;

    @BeforeEach
    void setUp() {
        createDTO = new MembershipCreateDTO();
        createDTO.setIdUsuario(1);
        createDTO.setFechaInicio(LocalDate.of(2025, 10, 1));
        createDTO.setFechaFin(LocalDate.of(2025, 10, 31));

        updateDTO = new MembershipUpdateDTO();
        updateDTO.setFechaFin(LocalDate.of(2025, 11, 30));

        membershipDTO = new MembershipDTO();
        membershipDTO.setIdMembresia(1);
        membershipDTO.setIdUsuario(1);
        membershipDTO.setFechaInicio(LocalDate.of(2025, 10, 1));
        membershipDTO.setFechaFin(LocalDate.of(2025, 10, 31));
        membershipDTO.setEstado(MembershipStatus.ACTIVA);
    }

    // -------------------------------
    // TEST #1 - CREATE
    // -------------------------------
    @Test
    @DisplayName("CREATE - Should create membership when data is valid")
    void testCreateMembership_ValidData() {
        when(membershipDAO.save(createDTO)).thenReturn(membershipDTO);

        MembershipDTO result = membershipServiceImpl.save(createDTO);

        assertThat(result).isNotNull();
        assertThat(result.getIdUsuario()).isEqualTo(createDTO.getIdUsuario());
        verify(membershipDAO).save(createDTO);
    }

    // -------------------------------
    // TEST #2 - UPDATE
    // -------------------------------
    @Test
    @DisplayName("UPDATE - Should update membership when it exists")
    void testUpdateMembership_Valid() {
        when(membershipDAO.update(1, updateDTO)).thenReturn(Optional.of(membershipDTO));

        Optional<MembershipDTO> updated = membershipServiceImpl.update(1, updateDTO);

        assertThat(updated).isPresent();
        verify(membershipDAO).update(1, updateDTO);
    }

    // -------------------------------
    // TEST #3 - FIND BY ID
    // -------------------------------
    @Test
    @DisplayName("FIND - Should return membership when found")
    void testFindById_Found() {
        when(membershipDAO.findById(1)).thenReturn(Optional.of(membershipDTO));

        Optional<MembershipDTO> found = membershipServiceImpl.findById(1);

        assertThat(found).isPresent();
        assertThat(found.get().getIdMembresia()).isEqualTo(1);
        verify(membershipDAO).findById(1);
    }

    // -------------------------------
    // TEST #4 - DELETE
    // -------------------------------
    @Test
    @DisplayName("DELETE - Should delete membership successfully")
    void testDeleteMembership() {
        when(membershipDAO.deleteById(1)).thenReturn(true);

        boolean deleted = membershipServiceImpl.delete(1);

        assertThat(deleted).isTrue();
        verify(membershipDAO).deleteById(1);
    }

    // -------------------------------
    // TEST #5 - ACTIVE CHECK
    // -------------------------------
    @Test
    @DisplayName("ACTIVE - Should verify if membership is active")
    void testIsMembershipActive() {
        when(membershipDAO.isMembershipActive(1)).thenReturn(true);

        boolean isActive = membershipServiceImpl.isMembershipActive(1);

        assertThat(isActive).isTrue();
        verify(membershipDAO).isMembershipActive(1);
    }
}
