package com.devumut.Notely;

import com.devumut.Notely.domain.dto.NotesDto;
import com.devumut.Notely.domain.dto.UserDto;
import com.devumut.Notely.domain.entities.NoteEntity;
import com.devumut.Notely.domain.entities.UserEntity;

import java.util.UUID;

public final class TestDataUtil {
    private TestDataUtil() {

    }

    public static UserEntity createTestUserEntityA() {
        return UserEntity.builder()
                .userId(UUID.randomUUID())
                .username("umut123")
                .password("12345")
                .build();
    }

    public static UserDto createTestUserDtoA() {
        return UserDto.builder()
                .userId(UUID.randomUUID())
                .username("umut123")
                .password("12345")
                .build();
    }

    public static UserEntity createTestUserEntityB() {
        return UserEntity.builder()
                .userId(UUID.randomUUID())
                .username("ali1")
                .password("1234")
                .build();
    }

    public static NoteEntity createTestNoteEntityA(UserEntity user) {
        return NoteEntity.builder()
                .noteTitle("Note titleA")
                .noteContent("Note contentA")
                .user(user)
                .build();
    }

    public static NotesDto createTestNoteDtoA(UserEntity user) {
        return NotesDto.builder()
                .noteTitle("Note titleA")
                .noteContent("Note contentA")
                .user(user)
                .build();
    }

    public static NoteEntity createTestNoteEntityB(UserEntity user) {
        return NoteEntity.builder()
                .noteTitle("Note titleB")
                .noteContent("Note contentB")
                .user(user)
                .build();
    }
}
