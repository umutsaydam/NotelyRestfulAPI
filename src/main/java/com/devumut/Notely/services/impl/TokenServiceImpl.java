package com.devumut.Notely.services.impl;

import com.devumut.Notely.domain.entities.TokenEntity;
import com.devumut.Notely.repositories.TokenRepository;
import com.devumut.Notely.services.TokenService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository repository;

    public TokenServiceImpl(TokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveToken(UUID userId, String token) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        repository.save(tokenEntity);
    }

    @Override
    public boolean isTokenValid(String token) {
        return repository.existByToken(token).isPresent();
    }

    @Override
    public void removeToken(String token) {
        repository.deleteByToken(token);
    }

    @Override
    public void removeTokensByUserId(UUID userId) {
        repository.deleteTokensByUserId(userId);
    }
}
