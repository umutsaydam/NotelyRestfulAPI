package com.devumut.Notely.repositories;

import com.devumut.Notely.TestDataUtil;
import com.devumut.Notely.domain.entities.NoteEntity;
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
public class NoteEntityRepositoryIntegrationTest {
    private final NoteRepository underTest;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public NoteEntityRepositoryIntegrationTest(NoteRepository underTest, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.underTest = underTest;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    public void testThatUserSaveNoteSuccessfully() {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity savedUser = userRepository.save(userEntity);

        NoteEntity noteEntity = TestDataUtil.createTestNoteEntityA(savedUser);
        NoteEntity savedNote = underTest.save(noteEntity);
        assertThat(savedNote).isEqualTo(noteEntity);
    }

    @Test
    public void testThatUserReadSavedNoteSuccessfully() {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity savedUser = userRepository.save(userEntity);

        NoteEntity noteEntity = TestDataUtil.createTestNoteEntityA(savedUser);
        NoteEntity savedNote = underTest.save(noteEntity);

        Optional<NoteEntity> note = underTest.findById(savedNote.getNoteId());
        assertThat(note).isPresent();
        assertThat(note.get()).isEqualTo(savedNote);
    }

    @Test
    public void testThatUserDeleteNoteSuccessfully() {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity savedUser = userRepository.save(userEntity);

        NoteEntity noteEntity = TestDataUtil.createTestNoteEntityA(savedUser);
        NoteEntity savedNote = underTest.save(noteEntity);
        underTest.deleteById(savedNote.getNoteId());

        Optional<NoteEntity> note = underTest.findById(savedNote.getNoteId());
        assertThat(note).isNotPresent();
    }
}
