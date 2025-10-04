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

    private boolean validRol(String rol) {
        if (rol == null) return false;
        String r = rol.trim().toUpperCase();
        return r.equals("MIEMBRO") || r.equals("USUARIO");
    }

    public UserDTO save(UserCreateDTO createDTO) {
        if (userRepository.existsByEmail(createDTO.getEmail())) {
            throw new IllegalArgumentException("Este correo ya está registrado.");
        }

        String rolToSet = createDTO.getRol() == null ? "USUARIO" : createDTO.getRol().trim().toUpperCase();
        if (!validRol(rolToSet)) {
            throw new IllegalArgumentException("Rol inválido. Solo se permite 'MIEMBRO' o 'USUARIO'.");
        }

        UserEntity entity = userMapper.toEntity(createDTO);
        entity.setRol(rolToSet);
        UserEntity savedEntity = userRepository.save(entity);
        return userMapper.toDTO(savedEntity);
    }

    public Optional<UserDTO> findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO);
    }

    public List<UserDTO> findAll() {
        List<UserEntity> entities = userRepository.findAll();
        return userMapper.toDTOList(entities);
    }
    public Optional<UserDTO> update(Integer id, UserUpdateDTO updateDTO) {
        return userRepository.findById(id)
                .map(existingEntity -> {
                    updateDTO.setIdUsuario(null);
                    updateDTO.setRol(null);
                    if (updateDTO.getNombre() != null && !updateDTO.getNombre().isBlank()) {
                        existingEntity.setNombre(updateDTO.getNombre());
                    }

                    if (updateDTO.getEmail() != null && !updateDTO.getEmail().isBlank()) {
                        existingEntity.setEmail(updateDTO.getEmail());
                    }

                    if (updateDTO.getTelefono() != null && !updateDTO.getTelefono().isBlank()) {
                        existingEntity.setTelefono(updateDTO.getTelefono());
                    }

                    if (updateDTO.getContrasena() != null && !updateDTO.getContrasena().isBlank()) {
                        existingEntity.setContrasena(updateDTO.getContrasena());
                    }

                    UserEntity updatedEntity = userRepository.save(existingEntity);
                    return userMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<UserDTO> login(String email, String contrasena) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getContrasena().equals(contrasena))
                .map(userMapper::toDTO);
    }
}
