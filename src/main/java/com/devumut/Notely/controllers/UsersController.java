package com.devumut.Notely.controllers;

import com.devumut.Notely.domain.dto.UserDto;
import com.devumut.Notely.domain.entities.UserEntity;
import com.devumut.Notely.jwt.JwtUtil;
import com.devumut.Notely.mappers.Mapper;
import com.devumut.Notely.services.TokenService;
import com.devumut.Notely.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/users")
public class UsersController {

    private final Mapper<UserEntity, UserDto> userMapper;
    private final UserService service;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    @Autowired
    public UsersController(Mapper<UserEntity, UserDto> userMapper, UserService service, JwtUtil jwtUtil, TokenService tokenService) {
        this.userMapper = userMapper;
        this.service = service;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        if (userDto.getUsername().length() > 30 || userDto.getPassword().length() > 30) {
            return new ResponseEntity<>("Kullanıcı adı ve parola 30 karakteri aşamaz.", HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.mapFrom(userDto);
        try {
            UserEntity loginUser = service.loginUser(userEntity);
            String token = jwtUtil.generateToken(loginUser.getUserId(), loginUser.getUsername());
            tokenService.removeTokensByUserId(loginUser.getUserId());
            tokenService.saveToken(loginUser.getUserId(), token);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // e.getMessage();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (!tokenService.isTokenValid(token)) {
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }
        tokenService.removeToken(token);
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }
}
