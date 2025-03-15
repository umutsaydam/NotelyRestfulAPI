package com.devumut.Notely.services.impl;

import com.devumut.Notely.domain.entities.UserEntity;
import com.devumut.Notely.repositories.UserRepository;
import com.devumut.Notely.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
    public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserEntity createUser(UserEntity newUser) {
        if (repository.findByUsername(newUser.getUsername()).isPresent()){
            throw new RuntimeException("This username has already taken!");
        }
        return repository.save(newUser);
    }
}
