package com.devumut.Notely.services;

import com.devumut.Notely.domain.entities.UserEntity;
import org.springframework.stereotype.Service;


public interface UserService {

    UserEntity createUser(UserEntity newUser);
}
