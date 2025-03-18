package com.devumut.Notely.repositories;

import com.devumut.Notely.domain.entities.TokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, UUID> {

    @Query("SELECT t FROM TokenEntity t WHERE t.token = ?1")
    Optional<TokenEntity> existByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM TokenEntity t WHERE t.token = ?1")
    void deleteByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM TokenEntity t WHERE t.userId = ?1")
    void deleteTokensByUserId(UUID userId);
}
