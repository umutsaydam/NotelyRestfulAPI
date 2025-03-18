package com.devumut.Notely.services;

import com.devumut.Notely.domain.entities.UserEntity;

import java.util.Optional;
import java.util.UUID;


public interface UserService {

    UserEntity createUser(UserEntity newUser);

    boolean isUserExistByUsername(String username);

    UserEntity loginUser(UserEntity user);
}
