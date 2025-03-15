package com.devumut.Notely.repositories;

import com.devumut.Notely.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {
    @Query("SELECT u FROM UserEntity u WHERE username = ?1")
    Optional<UserEntity> findByUsername(String username);
}
