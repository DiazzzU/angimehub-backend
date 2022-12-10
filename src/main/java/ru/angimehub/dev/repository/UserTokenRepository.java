package ru.angimehub.dev.repository;

import ru.angimehub.dev.entity.UserEntity;
import ru.angimehub.dev.entity.UserTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserTokenEntity, Integer> {
    Optional<UserTokenEntity> findOneByToken(String token);
    Optional<UserTokenEntity> findOneByUser(UserEntity user);
}
