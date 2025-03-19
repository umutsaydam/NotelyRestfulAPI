package com.devumut.Notely.controllers;

import com.devumut.Notely.TestDataUtil;
import com.devumut.Notely.domain.dto.UserDto;
import com.devumut.Notely.domain.entities.UserEntity;
import com.devumut.Notely.jwt.JwtUtil;
import com.devumut.Notely.services.TokenService;
import com.devumut.Notely.services.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {

    private final UserService userService;
    private final ObjectMapper mapper;
    private final MockMvc mockMvc;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserControllerIntegrationTests(UserService userService, ObjectMapper mapper, MockMvc mockMvc, TokenService tokenService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.mapper = mapper;
        this.mockMvc = mockMvc;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Test
    public void testThatUserRegisterSuccessfullyReturnsHttp201() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        String userJson = mapper.writeValueAsString(userEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatUserRegisterSuccessfullyReturnsSavedUser() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        String userJson = mapper.writeValueAsString(userEntity);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.userId").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value(userEntity.getUsername())
        ).andReturn();

        String responseJson = result.getResponse().getContentAsString();
        JsonNode jsonNode = mapper.readTree(responseJson);
        String hashedPassword = jsonNode.get("password").asText();

        assertThat(passwordEncoder.matches(userEntity.getPassword(), hashedPassword)).isTrue();
    }

    @Test
    public void testThatUserRegisterContainsTheSameUsernameReturnsHttp400BadRequest() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        String userJson = mapper.writeValueAsString(userEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatUserRegisterCharacterLimitExceededReturnsHttp400BadRequest() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userEntity.setUsername("ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        String userJson = mapper.writeValueAsString(userEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatUserLoginReturnsHttp200Ok() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userService.createUser(userEntity);

        UserEntity userEntityToLogin = TestDataUtil.createTestUserEntityA();
        userEntityToLogin.setUserId(null);

        String userJson = mapper.writeValueAsString(userEntityToLogin);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUserLoginReturnsToken() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userService.createUser(userEntity);

        UserEntity userEntityToLogin = TestDataUtil.createTestUserEntityA();
        userEntityToLogin.setUserId(null);

        String userJson = mapper.writeValueAsString(userEntityToLogin);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.token").isNotEmpty()
        );
    }

    @Test
    public void testThatUserLoginWithWrongUsername() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userService.createUser(userEntity);

        UserEntity changedUser = TestDataUtil.createTestUserEntityA();
        changedUser.setUsername("test");

        String userJson = mapper.writeValueAsString(changedUser);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        );
    }

    @Test
    public void testThatUserLogoutSuccessfullyReturnsHttp200Ok() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userService.createUser(userEntity);

        UserDto userDto = TestDataUtil.createTestUserDtoA();
        userDto.setUserId(null);
        String userDtoJson = mapper.writeValueAsString(userDto);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNode = mapper.readTree(jsonResponse);
        String token = jsonNode.get("token").asText();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/logout")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUserLogoutReturnsHttp200Forbidden() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        UserEntity createdUser = userService.createUser(userEntity);
        createdUser.setUsername("Changed username");

        String dummyToken = jwtUtil.generateToken(createdUser.getUserId(), createdUser.getUsername());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/logout")
                        .header("Authorization", "Bearer " + dummyToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }
}
