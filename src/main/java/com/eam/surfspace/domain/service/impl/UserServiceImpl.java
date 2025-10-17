package com.eam.surfspace.domain.service.impl;
import com.eam.surfspace.domain.dto.UserCreateDTO;
import com.eam.surfspace.domain.dto.UserDTO;
import com.eam.surfspace.domain.dto.UserUpdateDTO;
import com.eam.surfspace.persistence.dao.UserDAO;
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
public class UserServiceImpl implements UserService{
    private final UserDAO userDAO;

    @Override
    public UserDTO save(UserCreateDTO createDTO) {
        log.info("Creating a new user");
        return userDAO.save(createDTO);
    }

    @Override
    public Optional<UserDTO> update(Integer id, UserUpdateDTO updateDTO) {
        log.info("Updating user with id {}", id);
        return userDAO.update(id, updateDTO);
    }

    @Override
    public Optional<UserDTO> findById(Integer id) {
        log.info("Searching user with id {}", id);
        return userDAO.findById(id);
    }

    @Override
    public List<UserDTO> findAll() {
        log.info("Listing all users");
        return userDAO.findAll();
    }

    @Override
    public boolean delete(Integer id) {
        log.info("Deleting user with id {}", id);
        return userDAO.deleteById(id);
    }
    @Override
    public Optional<UserDTO> login(String email, String contrasena) {
        log.info("Attempting login for email {}", email);
        return userDAO.login(email, contrasena);
    }
}
