package com.eam.surfspace.domain;

import com.eam.surfspace.domain.dto.UserCreateDTO;
import com.eam.surfspace.domain.dto.UserDTO;
import com.eam.surfspace.domain.dto.UserUpdateDTO;
import com.eam.surfspace.domain.service.impl.UserServiceImpl;
import com.eam.surfspace.persistence.dao.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Test")
public class UserServiceTest {
    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userService;

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
        when(userDAO.save(validUserCreateDTO)).thenReturn(expectedUserDTO);

        UserDTO createdUser = userService.save(validUserCreateDTO);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getNombre()).isEqualTo(expectedUserDTO.getNombre());
        assertThat(createdUser.getEmail()).isEqualTo(expectedUserDTO.getEmail());
        assertThat(createdUser.getRol()).isEqualTo(expectedUserDTO.getRol());

        verify(userDAO).save(validUserCreateDTO);
    }

    @Test
    @DisplayName("UPDATE - Should update user info successfully")
    void testUpdateUser_ValidData() {
        Integer userId = 1;

        when(userDAO.update(userId, validUserUpdateDTO)).thenReturn(Optional.of(expectedUserDTO));

        Optional<UserDTO> updatedUser = userService.update(userId, validUserUpdateDTO);

        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getNombre()).isEqualTo(expectedUserDTO.getNombre());

        verify(userDAO).update(userId, validUserUpdateDTO);
    }

    @Test
    @DisplayName("FIND BY ID - Should return user when exists")
    void testFindUserById() {
        Integer userId = 1;
        when(userDAO.findById(userId)).thenReturn(Optional.of(expectedUserDTO));

        Optional<UserDTO> foundUser = userService.findById(userId);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(expectedUserDTO.getEmail());

        verify(userDAO).findById(userId);
    }

    @Test
    @DisplayName("FIND ALL - Should return list of users")
    void testFindAllUsers(){
        //ARRANGE
        List<UserDTO> users = List.of(expectedUserDTO); //creamos una lista con un user
        when(userDAO.findAll()).thenReturn(users); //configuramos el mock: cuando userDAO.findAll() sea llamado, que devuelva la lista users

        //ACT (ejecutamos el metodo que queremos probar)
        List<UserDTO> result = userService.findAll();

        //ASSERT (verificamos los resultados esperados)
        assertThat(result).isNotEmpty(); //comprobamos que la lista no esté vacía
        assertThat(result).hasSize(1); //que tenga un objeto en ella
        assertThat(result.get(0).getEmail()).isEqualTo(expectedUserDTO.getEmail()); //que el email sea igual

        verify(userDAO).findAll(); //verificamos que el metodo findAll() del DAO fue llamado
    }

    @Test
    @DisplayName("LOGIN - Should return user when credentials are correct")
    void testLogin_ValidCredentials(){
        //ARRANGE (definimos datos de prueba)
        String email = validUserCreateDTO.getEmail();
        String password = validUserCreateDTO.getContrasena();

        when(userDAO.login(email, password)).thenReturn(Optional.of(expectedUserDTO)); //cuando el DAO reciba estas credenciales, devuelve Optional con el usuario

        //ACT (llamamos al metodo login que vamos a probar
        Optional<UserDTO> result = userService.login(email, password);

        //ASSERT (comprobamos que el login fue exitoso)
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(expectedUserDTO.getEmail());

        verify(userDAO).login(email, password);
    }

    @Test
    @DisplayName("DELETE - Should delete user successfully")
    void testDeleteUser() {
        Integer userId = 1;

        when(userDAO.deleteById(userId)).thenReturn(true);

        boolean deleted = userService.delete(userId);

        assertThat(deleted).isTrue();

        verify(userDAO).deleteById(userId);
    }
}
