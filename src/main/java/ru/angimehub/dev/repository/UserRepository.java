package ru.angimehub.dev.repository;

import ru.angimehub.dev.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findOneByEmail(String email);
    Optional<UserEntity> findById(Integer id);
    List<UserEntity> findTop100ByRoleOrderByPopularnessDesc(String role);
    List<UserEntity> findByRole(String role);
}
