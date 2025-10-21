package com.eam.surfspace.domain.service;
import com.eam.surfspace.domain.dto.UserCreateDTO;
import com.eam.surfspace.domain.dto.UserDTO;
import com.eam.surfspace.domain.dto.UserUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO save(UserCreateDTO createDTO);

    Optional<UserDTO> update(Integer id, UserUpdateDTO updateDTO);

    Optional<UserDTO> findById(Integer id);

    List<UserDTO> findAll();

    boolean delete(Integer id);

    Optional<UserDTO> login(String email, String contrasena);
}
