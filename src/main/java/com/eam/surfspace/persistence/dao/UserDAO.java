package com.eam.surfspace.persistence.dao;
import com.eam.surfspace.domain.dto.UserCreateDTO;
import com.eam.surfspace.domain.dto.UserDTO;
import com.eam.surfspace.domain.dto.UserUpdateDTO;
import com.eam.surfspace.persistence.entity.UserEntity;
import com.eam.surfspace.persistence.mapper.UserMapper;
import com.eam.surfspace.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDAO {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Crear usuario
    public UserDTO save(UserCreateDTO createDTO) {
        if (userRepository.existsByEmail(createDTO.getEmail())) {
            throw new IllegalArgumentException("Este email ya está registrado.");
        }
        UserEntity entity = userMapper.toEntity(createDTO);
        UserEntity savedEntity = userRepository.save(entity);
        return userMapper.toDTO(savedEntity);
    }

    // Buscar usuario por ID
    public Optional<UserDTO> findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO);
    }

    // Listar todos los usuarios
    public List<UserDTO> findAll() {
        List<UserEntity> entities = userRepository.findAll();
        return userMapper.toDTOList(entities);
    }

    // Actualizar usuario
    public Optional<UserDTO> update(Integer id, UserUpdateDTO updateDTO) {
        return userRepository.findById(id)
                .map(existingEntity -> {
                    userMapper.updateEntityFromDTO(updateDTO, existingEntity);
                    UserEntity updatedEntity = userRepository.save(existingEntity);
                    return userMapper.toDTO(updatedEntity);
                });
    }

    // Eliminar usuario por ID
    public boolean deleteById(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public Optional<UserDTO> login(String email, String contrasena) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getContrasena().equals(contrasena)) // aquí deberías usar encriptación
                .map(userMapper::toDTO);
    }
}
