package org.bright.springsecurity.repository;

import org.bright.springsecurity.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameAndIsActive(String username, boolean isActive);
}
