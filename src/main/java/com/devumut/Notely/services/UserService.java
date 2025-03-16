package com.devumut.Notely.services;

import com.devumut.Notely.domain.entities.UserEntity;


public interface UserService {

    UserEntity createUser(UserEntity newUser);

    boolean isUserExistByUsername(String username);

    UserEntity loginUser(UserEntity user);
}
