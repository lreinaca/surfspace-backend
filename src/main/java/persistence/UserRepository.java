package persistence;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public class UserRepository {

    @Transactional
    public UserEntity save(UserEntity user) {
        return user;
    }

    public Optional<UserEntity> findById(int id) {
        return null;
    }


    public List<UserEntity> findAll() {
        return null;
    }


    @Transactional
    public Optional<UserEntity> update(Integer id, UserEntity user) {
        return null;
    }


    @Transactional
    public void deleteById(int id) {

    }


}
