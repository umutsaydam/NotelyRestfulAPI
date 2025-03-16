package com.devumut.Notely.controllers;

import com.devumut.Notely.domain.dto.UserDto;
import com.devumut.Notely.domain.entities.UserEntity;
import com.devumut.Notely.mappers.Mapper;
import com.devumut.Notely.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/users")
public class UsersController {

    private final Mapper<UserEntity, UserDto> userMapper;
    private final UserService service;

    @Autowired
    public UsersController(Mapper<UserEntity, UserDto> userMapper, UserService service) {
        this.userMapper = userMapper;
        this.service = service;
    }


    @PostMapping(path = "/create")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        if (userDto.getUsername().length() > 30) {
            return new ResponseEntity<>("Kullanıcı adı 30 karakteri aşamaz.", HttpStatus.BAD_REQUEST);
        }
        UserEntity newUser = userMapper.mapFrom(userDto);
        try {
            UserEntity savedUserEntity = service.createUser(newUser);
            UserDto savedUserDto = userMapper.mapTo(savedUserEntity);
            return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // e.getMessage()
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto userDto){
        UserEntity userEntity = userMapper.mapFrom(userDto);
        try{
            UserEntity loginUser = service.loginUser(userEntity);
            return new ResponseEntity<>(userMapper.mapTo(loginUser), HttpStatus.OK);
        }catch (Exception e){
            // e.getMessage();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
