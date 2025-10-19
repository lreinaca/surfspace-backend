package com.eam.surfspace.domain;

import com.eam.surfspace.domain.dto.SpaceDTO;
import com.eam.surfspace.domain.service.impl.SpaceServiceImpl;
import com.eam.surfspace.persistence.dao.BookingDAO;
import com.eam.surfspace.persistence.dao.SpaceDAO;
import com.eam.surfspace.persistence.entity.EnumSpaceStatus;
import com.eam.surfspace.persistence.entity.EnumSpaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.eam.surfspace.persistence.entity.EnumPaymentStatus.PENDIENTE;
import static com.eam.surfspace.persistence.entity.EnumSpaceStatus.*;
import static com.eam.surfspace.persistence.entity.EnumSpaceType.ESPACIO_COMPARTIDO;
import static com.eam.surfspace.persistence.entity.EnumSpaceType.SALA_DE_REUNION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
    private Integer validSpaceId;

    // Configuración inicial antes de cada prueba
    @BeforeEach
    void setUp(){ //inicializamos los datos de prueba
        validSpaceId = 1;
        validSpaceDTO = new SpaceDTO();
                validSpaceDTO.setIdSpace(validSpaceId);
                validSpaceDTO.setName("Espacio de trabajo de prueba");
                validSpaceDTO.setDescription("un espacio amplio con los recursos necesarios");
                validSpaceDTO.setType(SALA_DE_REUNION);
                validSpaceDTO.setCapacity(20);
                validSpaceDTO.setStatus(DISPONIBLE);
    }

    // TEST DE CREAR ESPACIOS ================================================================================
    @Test
    @DisplayName("CREATE - valid space must return the created space with ID")
    void createSpaceTest_ValidData(){
        //ARRANGE (Preparar escenario)---------------------------------------
        SpaceDTO newSpaceDTO = new SpaceDTO();
                newSpaceDTO.setName("Espacio de trabajo de ejemplo 1");
                newSpaceDTO.setCapacity(25);
                newSpaceDTO.setDescription("un espacio amplio y agradable con los recursos necesarios");
                newSpaceDTO.setType(SALA_DE_REUNION);
                newSpaceDTO.setStatus(DISPONIBLE);

        SpaceDTO persistedSpaceDTO = new SpaceDTO();
                persistedSpaceDTO.setIdSpace(validSpaceId);
                persistedSpaceDTO.setName(newSpaceDTO.getName());
                persistedSpaceDTO.setCapacity(newSpaceDTO.getCapacity());
                persistedSpaceDTO.setDescription(newSpaceDTO.getDescription());
                persistedSpaceDTO.setType(newSpaceDTO.getType());
                persistedSpaceDTO.setStatus(newSpaceDTO.getStatus());

        //mockear comportamientos
        when(spaceDAO.save(any(SpaceDTO.class))).thenReturn(persistedSpaceDTO);

        //ACT ----------------------------------------------------------------
        SpaceDTO result = spaceService.createSpace(newSpaceDTO);

        //ASSERT -------------------------------------------------------------
        assertThat(result).isNotNull();
        assertThat(result.getIdSpace()).isEqualTo(validSpaceId);

        ArgumentCaptor<SpaceDTO> captor = ArgumentCaptor.forClass(SpaceDTO.class);
        verify(spaceDAO, times(1)).save(captor.capture());
        SpaceDTO passed = captor.getValue();
        assertThat(passed.getIdSpace()).isNull();
        assertThat(passed.getName()).isEqualTo(newSpaceDTO.getName());
    }

    //validaciones
    @Test
    @DisplayName("CREATE - empty state must throw IllegalArgumentException")
    void validateSpaceData_NullStatus_ShouldThrowException(){
        //ARRANGE (Preparar escenario)---------------------------------------
        validSpaceDTO.setStatus(null);

        //ACT & ASSERT ------------------------------------------------------
        assertThatThrownBy(()-> spaceService.createSpace(validSpaceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Se debe definir el estado del espacio, no debe ser nulo");

        //verificar que no se llamó al DAO
        verify(spaceDAO, never()).save(any(SpaceDTO.class));
    }

    @Test
    @DisplayName("CREATE - if type is null it should throw IllegalArgumentException")
    void validateSpaceData_NullType_ShouldThrowException(){
        //ARRANGE (Preparar escenario)---------------------------------------
        validSpaceDTO.setType(null);

        //ACT & ASSERT
        assertThatThrownBy(()-> spaceService.createSpace(validSpaceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Se debe definir el tipo de espacio, no puede ser nulo");

        //verificar que no se llamó al DAO
        verify(spaceDAO, never()).save(any(SpaceDTO.class));
    }

    @Test
    @DisplayName("CREATE - Empty name should throw IllegalArgumentException")
    void validateSpaceData_NullName_ShouldThrowException(){
        //ARRANGE (Preparar escenario)---------------------------------------
        validSpaceDTO.setName(null);

        //ACT & ASSERT
        assertThatThrownBy(()-> spaceService.createSpace(validSpaceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El espacio debe tener un nombre, no puede ser nulo");

        //verificar que no se llamó al DAO
        verify(spaceDAO, never()).save(any(SpaceDTO.class));
    }

    @Test
    @DisplayName("CREATE - Empty description should throw IllegalArgumentException")
    void validateSpaceData_NullDescription_ShouldThrowException(){
        //ARRANGE (Preparar escenario)---------------------------------------
        validSpaceDTO.setDescription(null);

        //ACT & ASSERT
        assertThatThrownBy(()-> spaceService.createSpace(validSpaceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El espacio debe tener una descripción, no puede ser nula");

        //verificar que no se llamó al DAO
        verify(spaceDAO, never()).save(any(SpaceDTO.class));
    }

    @Test
    @DisplayName("CREATE - Empty description should throw IllegalArgumentException")
    void validateSpaceData_InvalidCapacity_ShouldThrowException(){
        //ARRANGE (Preparar escenario)---------------------------------------
        validSpaceDTO.setCapacity(0);

        //ACT & ASSERT
        assertThatThrownBy(()-> spaceService.createSpace(validSpaceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La capacidad debe ser un número positivo");

        //verificar que no se llamó al DAO
        verify(spaceDAO, never()).save(any(SpaceDTO.class));
    }

    // TEST DE TRAER ESPACIOS ================================================================================
    @Test
    @DisplayName("READ ALL - Must return list of spaces")
    void getAllSpaces_ShouldReturnSpaceList(){
        // ARRANGE
        List<SpaceDTO> spaces = Arrays.asList(
                new SpaceDTO(1, "Espacio 101", 25, "espacio de trabajo", SALA_DE_REUNION, DISPONIBLE),
                new SpaceDTO(2, "Espacio 102", 30, "espacio de trabajo", ESPACIO_COMPARTIDO, INACTIVO),
                new SpaceDTO(3, "Espacio 103", 20, "espacio de trabajo", SALA_DE_REUNION, OCUPADO)
        );

        when(spaceDAO.findAll()).thenReturn(spaces);

        // ACT
        List<SpaceDTO> result = spaceService.getAllSpaces();

        // ASSERT
        assertThat(result).hasSize(3);
        assertThat(result).extracting("name")
                .containsExactly("Espacio 101", "Espacio 102", "Espacio 103");

        verify(spaceDAO, times(1)).findAll();
    }

    // TEST DE OBTENER ESPACIO POR ID ========================================================================
    @Test
    @DisplayName("READ - Existing space must return space")
    void getSpaceById_ExistingId_ShouldReturnSpace(){
        // ARRANGE (Preparar escenario)
        SpaceDTO existingSpace = new SpaceDTO(
                validSpaceId,
                "Espacio 101",
                25,
                "espacio de trabajo",
                SALA_DE_REUNION,
                DISPONIBLE
        );

        when(spaceDAO.findById(validSpaceId))
                .thenReturn(Optional.of(existingSpace));

        //ACT (ejecutamos la acción)
        SpaceDTO result = spaceService.getSpaceById(validSpaceId);

        //ASSERT (verificamos los resultados)
        assertThat(result).isNotNull();
        assertThat(result.getIdSpace()).isEqualTo(validSpaceId);
        assertThat(result.getName()).isEqualTo("Espacio 101");

        verify(spaceDAO, times(1)).findById(validSpaceId);
    }

    @Test
    @DisplayName("READ - Nonexistent space should throw RuntimeException")
    void getSpaceById_NonExistentId_ShouldThrowException(){
        // ARRANGE
        Integer nonExistentId = 999;
        when(spaceDAO.findById(nonExistentId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> spaceService.getSpaceById(nonExistentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Space not found with ID: " + nonExistentId);

        verify(spaceDAO, times(1)).findById(nonExistentId);
    }

    // TEST DE ACTUALIZAR UN ESPACIO =========================================================================
    @Test
    @DisplayName("UPDATE - Valid update must return updated space")
    void updateSpace_ValidData_ShouldReturnUpdatedSpace(){
        //ARRANGE -----------------------------------------
        //creamos un espacio existente
        SpaceDTO existingSpace = new SpaceDTO(
                validSpaceId,
                "Espacio existente",
                10,
                "Descripcion existente",
                SALA_DE_REUNION,
                MANTENIMIENTO
        );

        //creamos los nuevos datos
        SpaceDTO updateData = new SpaceDTO(
                validSpaceId,
                "Espacio actualizado",
                25,
                "Descripción actualizada",
                ESPACIO_COMPARTIDO,
                DISPONIBLE
        );

        //lo que el DAO devolvería si se actualizó correctamente
        SpaceDTO updatedSpace = new SpaceDTO(
                validSpaceId,
                "Espacio actualizado",
                25,
                "Descripción actualizada",
                ESPACIO_COMPARTIDO,
                DISPONIBLE
        );

        //Cuando se busque el espacio por ID, devolvemos el existente
        when(spaceDAO.findById(validSpaceId)).thenReturn(Optional.of(existingSpace));

        //Cuando se actualice el espacio, devolvemos el actualizado
        when(spaceDAO.updateSpace(eq(validSpaceId), any(SpaceDTO.class)))
                .thenReturn(Optional.of(updatedSpace));

        //ACT (ejecutamos el metodo) -----------------------
        SpaceDTO result = spaceService.updateSpace(validSpaceId, updateData);

        //ASSERT -------------------------------------------
        assertThat(result).isNotNull();
        assertThat(result.getIdSpace()).isEqualTo(validSpaceId);
        assertThat(result.getName()).isEqualTo("Espacio actualizado");
        assertThat(result.getCapacity()).isEqualTo(25);
        assertThat(result.getDescription()).isEqualTo("Descripción actualizada");
        assertThat(result.getType()).isEqualTo(ESPACIO_COMPARTIDO);
        assertThat(result.getStatus()).isEqualTo(DISPONIBLE);

        verify(spaceDAO, times(1)).updateSpace(eq(validSpaceId), any(SpaceDTO.class));
    }

    @Test
    @DisplayName("UPDATE - Nonexistent space should throw RuntimeException")
    void updateSpace_NonExistentId_ShouldThrowException(){
        //ARRANGE -----------------------------------------
        Integer nonExistentId = 999;

        //simulamos que no se encontró el espacio
        when(spaceDAO.findById(nonExistentId))
                .thenReturn(Optional.empty());

        //ACT (ejecutamos el metodo) -----------------------
        assertThatThrownBy(()-> spaceService.updateSpace(nonExistentId,validSpaceDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Space not found with ID: " + nonExistentId);

        verify(spaceDAO, never()).updateSpace(anyInt(), any());
    }

    //validaciones
    @Test
    @DisplayName("UPDATE - empty state must throw IllegalArgumentException")
    void updateSpace_NullStatus_ShouldThrowException(){
        //ARRANGE (Preparar escenario)---------------------------------------
        validSpaceDTO.setStatus(null);

        //ACT & ASSERT ------------------------------------------------------
        assertThatThrownBy(()-> spaceService.updateSpace(validSpaceId, validSpaceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Se debe definir el estado del espacio, no debe ser nulo");

        verify(spaceDAO, never()).updateSpace(anyInt(), any());
    }

    @Test
    @DisplayName("UPDATE - Null name must throw IllegalArgumentException")
    void updateSpace_NullName_ShouldThrowException(){
        //ARRANGE (Preparar escenario)---------------------------------------
        validSpaceDTO.setName(null);

        //ACT & ASSERT ------------------------------------------------------
        assertThatThrownBy(() -> spaceService.updateSpace(validSpaceId, validSpaceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El espacio debe tener un nombre, no puede ser nulo");

        verify(spaceDAO, never()).updateSpace(anyInt(), any());
    }

    @Test
    @DisplayName("UPDATE - Null description should throw IllegalArgumentException")
    void updateSpace_NullDescription_ShouldThrowException(){
        //ARRANGE (Preparar escenario)---------------------------------------
        validSpaceDTO.setDescription(null);

        //ACT & ASSERT ------------------------------------------------------
        assertThatThrownBy(() -> spaceService.updateSpace(validSpaceId, validSpaceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El espacio debe tener una descripción, no puede ser nula");

        verify(spaceDAO, never()).updateSpace(anyInt(), any());
    }

    @Test
    @DisplayName("UPDATE - Invalid capability should throw IllegalArgumentException")
    void updateSpace_InvalidCapacity_ShouldThrowException(){
        //ARRANGE (Preparar escenario)---------------------------------------
        validSpaceDTO.setCapacity(0);

        //ACT & ASSERT ------------------------------------------------------
        assertThatThrownBy(() -> spaceService.updateSpace(validSpaceId, validSpaceDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La capacidad debe ser un número positivo");

        verify(spaceDAO, never()).updateSpace(anyInt(), any());
    }

    // TEST DE DESACTIVAR UN ESPACIO =========================================================================
    @Test
    @DisplayName("DEACTIVATE - Existing space must be set to INACTIVO and saved")
    void deactivateSpace_ExistingSpace_ShouldSetStatusToInactiveAndSave(){
        //ARRANGE (Preparar escenario)---------------------------------------
        //simulamos un espacio con estado DISPONIBLE
        SpaceDTO existingSpace = new SpaceDTO(
                validSpaceId,
                "Espacio a probarr",
                25,
                "Espacio de prueba",
                SALA_DE_REUNION,
                DISPONIBLE
        );

        when(spaceDAO.findById(validSpaceId)).thenReturn(Optional.of(existingSpace));

        //ACT (ejecutamos la acción)
        spaceService.deactivateSpace(validSpaceId);

        //ASSERT (verificamos los resultados)
        // verificamos que el estado del espacio fue cambiado a INACTIVO
        assertThat(existingSpace.getStatus()).isEqualTo(INACTIVO);

        verify(spaceDAO, times(1)).save(existingSpace);
    }

    @Test
    @DisplayName("DEACTIVATE - Nonexistent space should throw RuntimeException")
    void deactivateSpace_NonExistentId_ShouldThrowRuntimeException() {
        //ARRANGE (Preparar escenario)---------------------------------------
        Integer nonExistentId = 999;

        //simulamos que el espacio no existe
        when(spaceDAO.findById(nonExistentId)).thenReturn(Optional.empty());

        //ACT & ASSERT ------------------------------------------------------
        //esperamos que se lance una excepción al intentar desactivarlo
        assertThatThrownBy(() -> spaceService.deactivateSpace(nonExistentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Space not found with ID: " + nonExistentId);

        //verificamos que NUNCA se llame a save()
        verify(spaceDAO, never()).save(any(SpaceDTO.class));
    }

}
