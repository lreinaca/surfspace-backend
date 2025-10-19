package com.eam.surfspace.domain;

import com.eam.surfspace.domain.dto.UserCreateDTO;
import com.eam.surfspace.domain.dto.UserDTO;
import com.eam.surfspace.domain.dto.UserUpdateDTO;
import com.eam.surfspace.domain.service.UserService;
import com.eam.surfspace.domain.service.impl.UserServiceImpl;
import com.eam.surfspace.persistence.dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Test")
public class UserServiceTest {
    @Mock
    private UserDAO userDAO;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private UserCreateDTO validUserCreateDTO;
    private UserUpdateDTO validUserUpdateDTO;
    private UserDTO expectedUserDTO;

    @BeforeEach
    void setUp() {
        validUserCreateDTO = new UserCreateDTO();
        validUserCreateDTO.setNombre("Juan Pérez");
        validUserCreateDTO.setEmail("juanperez@example.com");
        validUserCreateDTO.setContrasena("123456");
        validUserCreateDTO.setRol("VISITANTE");

        expectedUserDTO = new UserDTO();
        expectedUserDTO.setId(1);
        expectedUserDTO.setNombre(validUserCreateDTO.getNombre());
        expectedUserDTO.setEmail(validUserCreateDTO.getEmail());
        expectedUserDTO.setRol(validUserCreateDTO.getRol());

        validUserUpdateDTO = new UserUpdateDTO();
        validUserUpdateDTO.setNombre("Juan P. Actualizado");
        validUserUpdateDTO.setEmail("juan.actualizado@example.com");
        validUserUpdateDTO.setRol("AFILIADO");
    }

    @Test
    @DisplayName("CREATE - Should create a user when data is valid")
    void testCreateUser_ValidData() {
        when(userService.save(validUserCreateDTO)).thenReturn(expectedUserDTO);
        when(userDAO.save(validUserCreateDTO)).thenReturn(expectedUserDTO);

        UserDTO createdUser = userService.save(validUserCreateDTO);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getNombre()).isEqualTo(expectedUserDTO.getNombre());
        assertThat(createdUser.getEmail()).isEqualTo(expectedUserDTO.getEmail());
        assertThat(createdUser.getRol()).isEqualTo(expectedUserDTO.getRol());

        verify(userService).save(validUserCreateDTO);
        verify(userDAO).save(validUserCreateDTO);
    }

    @Test
    @DisplayName("UPDATE - Should update user info successfully")
    void testUpdateUser_ValidData() {
        Integer userId = 1;

        when(userService.update(userId, validUserUpdateDTO)).thenReturn(Optional.of(expectedUserDTO));
        when(userDAO.update(userId, validUserUpdateDTO)).thenReturn(Optional.of(expectedUserDTO));

        Optional<UserDTO> updatedUser = userService.update(userId, validUserUpdateDTO);

        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getNombre()).isEqualTo(expectedUserDTO.getNombre());

        verify(userService).update(userId, validUserUpdateDTO);
        verify(userDAO).update(userId, validUserUpdateDTO);
    }

    @Test
    @DisplayName("FIND BY ID - Should return user when exists")
    void testFindUserById() {
        Integer userId = 1;
        when(userService.findById(userId)).thenReturn(Optional.of(expectedUserDTO));
        when(userDAO.findById(userId)).thenReturn(Optional.of(expectedUserDTO));

        Optional<UserDTO> foundUser = userService.findById(userId);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(expectedUserDTO.getEmail());

        verify(userService).findById(userId);
        verify(userDAO).findById(userId);
    }

    @Test
    @DisplayName("DELETE - Should delete user successfully")
    void testDeleteUser() {
        Integer userId = 1;

        when(userService.delete(userId)).thenReturn(true);
        when(userDAO.deleteById(userId)).thenReturn(true);

        boolean deleted = userService.delete(userId);

        assertThat(deleted).isTrue();

        verify(userService).delete(userId);
        verify(userDAO).deleteById(userId);
    }
}
