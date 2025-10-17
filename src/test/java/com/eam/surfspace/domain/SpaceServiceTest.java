package com.eam.surfspace.domain;

import com.eam.surfspace.domain.dto.SpaceDTO;
import com.eam.surfspace.domain.service.impl.SpaceServiceImpl;
import com.eam.surfspace.persistence.dao.BookingDAO;
import com.eam.surfspace.persistence.dao.SpaceDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.eam.surfspace.persistence.entity.EnumSpaceStatus.DISPONIBLE;
import static com.eam.surfspace.persistence.entity.EnumSpaceType.SALA_DE_REUNION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //permite que @Mock y @InjectMocks funcionen automáticamente
@DisplayName("Space Service Unit Test")
public class SpaceServiceTest {

    //mocks que simulan comportamientos de dependencias reales
    @Mock
    private SpaceDAO spaceDAO;

    @Mock
    private BookingDAO bookingDAO;

    @InjectMocks
    private SpaceServiceImpl spaceService; //SUT (System Under Test)

    //datos de prueba
    private SpaceDTO validSpaceDTO;

    @BeforeEach
    void setUp(){ //inicializamos los datos de prueba
        validSpaceDTO = new SpaceDTO(
                123,
                "Espacio de prueba",
                25,
                "Un espacio creado especificamente para realizar pruebas unitarias",
                SALA_DE_REUNION,
                DISPONIBLE
        );
    }

    // TEST DE CREAR ESPACIOS ======================================================================
    @Test
    @DisplayName("Crear - debe retornar espacio creado exitosamente con id")
    void createSpaceTest_ValidData(){
        //ARRANGE (Preparar escenario)---------------------------------------
        SpaceDTO newSpaceDTO = new SpaceDTO(
                validSpaceDTO.getIdSpace(),
                validSpaceDTO.getName(),
                validSpaceDTO.getCapacity(),
                validSpaceDTO.getDescription(),
                validSpaceDTO.getType(),
                validSpaceDTO.getStatus()
        );

        //mockear comportamientos
        when(spaceDAO.save(any(SpaceDTO.class))).thenReturn(newSpaceDTO);

        //ACT ----------------------------------------------------------------
        SpaceDTO result = spaceService.createSpace(newSpaceDTO);

        //ASSERT -------------------------------------------------------------
        assertThat(result).isNotNull();
        assertThat(result.getIdSpace()).isEqualTo(newSpaceDTO.getIdSpace());
        verify(spaceDAO, times(1)).save(any(SpaceDTO.class));
    }

    // TEST DE TRAER ESPACIOS ======================================================================
    
}
