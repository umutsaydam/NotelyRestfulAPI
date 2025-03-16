package com.devumut.Notely.services.impl;

import com.devumut.Notely.domain.entities.UserEntity;
import com.devumut.Notely.repositories.UserRepository;
import com.devumut.Notely.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity createUser(UserEntity newUser) {
        if (isUserExistByUsername(newUser.getUsername())) {
            throw new RuntimeException("This username has already taken!");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return repository.save(newUser);
    }

    @Override
    public boolean isUserExistByUsername(String username) {
        return repository.findByUsername(username).isPresent();
    }

    @Override
    public UserEntity loginUser(UserEntity user) {
        Optional<UserEntity> optionalUserEntity = repository.findByUsername(user.getUsername());
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            if (passwordEncoder.matches(user.getPassword(), userEntity.getPassword())) {
                return userEntity;
            }
        }
        throw new RuntimeException("Username or password is wrong.");
    }
}
