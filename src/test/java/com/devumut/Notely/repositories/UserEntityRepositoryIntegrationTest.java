package com.devumut.Notely.repositories;

import com.devumut.Notely.TestDataUtil;
import com.devumut.Notely.domain.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserEntityRepositoryIntegrationTest {

    private final UserRepository underTest;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserEntityRepositoryIntegrationTest(UserRepository underTest, PasswordEncoder passwordEncoder) {
        this.underTest = underTest;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    public void testThatUserCanBeRegister() {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity savedUser = underTest.save(userEntity);

        Optional<UserEntity> result = underTest.findById(savedUser.getUserId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedUser);
    }

    @Test
    public void testThatUserCanLoginSuccessfully() {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity savedUser = underTest.save(userEntity);

        UserEntity loginUser = TestDataUtil.createTestUserEntityA();
        loginUser.setUserId(null);

        Optional<UserEntity> optionalUserEntity = underTest.findByUsername(loginUser.getUsername());
        assertThat(optionalUserEntity).isPresent();
        UserEntity user = optionalUserEntity.get();
        assertThat(passwordEncoder.matches(loginUser.getPassword(), savedUser.getPassword())).isTrue();
    }
}
