package com.devumut.Notely.controllers;

import com.devumut.Notely.TestDataUtil;
import com.devumut.Notely.domain.dto.NotesDto;
import com.devumut.Notely.domain.dto.UserDto;
import com.devumut.Notely.domain.entities.NoteEntity;
import com.devumut.Notely.domain.entities.UserEntity;
import com.devumut.Notely.services.NoteService;
import com.devumut.Notely.services.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class NoteControllerIntegrationTests {

    private final NoteService noteService;
    private final UserService userService;
    private final ObjectMapper mapper;
    private final MockMvc mockMvc;

    @Autowired
    public NoteControllerIntegrationTests(NoteService noteService, UserService userService, ObjectMapper mapper, MockMvc mockMvc) {
        this.noteService = noteService;
        this.userService = userService;
        this.mapper = mapper;
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatCreateNoteSuccessfullyReturnsHttp201Created() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userService.createUser(userEntity);

        UserDto userToLogin = TestDataUtil.createTestUserDtoA();
        userToLogin.setUserId(null);
        String userJson = mapper.writeValueAsString(userToLogin);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = mapper.readTree(response);
        String token = jsonNode.get("token").asText();

        NotesDto notesDto = TestDataUtil.createTestNoteDtoA(null);
        notesDto.setNoteId(null);
        String noteJson = mapper.writeValueAsString(notesDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/notes/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noteJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateNoteSuccessfullyReturnsSavedNote() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userService.createUser(userEntity);

        UserDto userToLogin = TestDataUtil.createTestUserDtoA();
        userToLogin.setUserId(null);
        String userJson = mapper.writeValueAsString(userToLogin);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = mapper.readTree(response);
        String token = jsonNode.get("token").asText();

        NotesDto notesDto = TestDataUtil.createTestNoteDtoA(null);
        notesDto.setNoteId(null);
        String noteJson = mapper.writeValueAsString(notesDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/notes/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noteJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.noteId").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.noteTitle").value(notesDto.getNoteTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.noteContent").value(notesDto.getNoteContent())
        );
    }

    @Test
    public void testThatCreateNoteWithEmptyContentReturns400BadRequest() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        userService.createUser(userEntity);

        UserDto userToLogin = TestDataUtil.createTestUserDtoA();
        userToLogin.setUserId(null);
        String userJson = mapper.writeValueAsString(userToLogin);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = mapper.readTree(response);
        String token = jsonNode.get("token").asText();

        NotesDto notesDto = TestDataUtil.createTestNoteDtoA(null);
        notesDto.setNoteId(null);
        notesDto.setNoteContent("");
        String noteJson = mapper.writeValueAsString(notesDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/notes/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noteJson)
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        );
    }

    @Test
    public void testThatReadNoteSuccessfullyByUserTokenAndNoteIdReturnsHttp200Ok() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        UserEntity savedUser = userService.createUser(userEntity);

        UserDto userToLogin = TestDataUtil.createTestUserDtoA();
        userToLogin.setUserId(null);
        String userJson = mapper.writeValueAsString(userToLogin);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = mapper.readTree(response);
        String token = jsonNode.get("token").asText();

        NoteEntity noteEntity = TestDataUtil.createTestNoteEntityA(savedUser);
        noteEntity.setNoteId(null);
        NoteEntity savedNote = noteService.createNote(noteEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/notes/read/" + savedNote.getNoteId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatReadNoteSuccessfullyByUserTokenAndNoteIdReturnsNote() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        UserEntity savedUser = userService.createUser(userEntity);

        UserDto userToLogin = TestDataUtil.createTestUserDtoA();
        userToLogin.setUserId(null);
        String userJson = mapper.writeValueAsString(userToLogin);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = mapper.readTree(response);
        String token = jsonNode.get("token").asText();

        NoteEntity noteEntity = TestDataUtil.createTestNoteEntityA(savedUser);
        noteEntity.setNoteId(null);
        NoteEntity savedNote = noteService.createNote(noteEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/notes/read/" + savedNote.getNoteId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.noteId").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.noteTitle").value(savedNote.getNoteTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.noteContent").value(savedNote.getNoteContent())
        );
    }

    @Test
    public void testThatReadNoteSuccessfullyByUserTokenAndWrongNoteIdReturnsHttp404NotFound() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        UserEntity savedUser = userService.createUser(userEntity);

        UserDto userToLogin = TestDataUtil.createTestUserDtoA();
        userToLogin.setUserId(null);
        String userJson = mapper.writeValueAsString(userToLogin);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = mapper.readTree(response);
        String token = jsonNode.get("token").asText();

        NoteEntity noteEntity = TestDataUtil.createTestNoteEntityA(savedUser);
        noteEntity.setNoteId(null);
        NoteEntity savedNote = noteService.createNote(noteEntity);
        savedNote.setNoteId(UUID.randomUUID());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/notes/read/" + savedNote.getNoteId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatUpdateNoteSuccessfullyByUserTokenAndNoteIdReturns200Ok() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        UserEntity savedUser = userService.createUser(userEntity);

        UserDto userToLogin = TestDataUtil.createTestUserDtoA();
        userToLogin.setUserId(null);
        String userJson = mapper.writeValueAsString(userToLogin);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = mapper.readTree(response);
        String token = jsonNode.get("token").asText();

        NoteEntity noteEntity = TestDataUtil.createTestNoteEntityA(savedUser);
        noteEntity.setNoteId(null);
        NoteEntity savedNote = noteService.createNote(noteEntity);
        savedNote.setNoteContent("UpdatedTitle");
        String savedNoteJson = mapper.writeValueAsString(savedNote);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/notes/update")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(savedNoteJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdateNoteSuccessfullyByUserTokenAndNoteIdReturnsUpdatedNote() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        UserEntity savedUser = userService.createUser(userEntity);

        UserDto userToLogin = TestDataUtil.createTestUserDtoA();
        userToLogin.setUserId(null);
        String userJson = mapper.writeValueAsString(userToLogin);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = mapper.readTree(response);
        String token = jsonNode.get("token").asText();

        NoteEntity noteEntity = TestDataUtil.createTestNoteEntityA(savedUser);
        noteEntity.setNoteId(null);
        NoteEntity savedNote = noteService.createNote(noteEntity);
        savedNote.setNoteContent("UpdatedTitle");
        String savedNoteJson = mapper.writeValueAsString(savedNote);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/notes/update")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(savedNoteJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.noteId").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.noteTitle").value(savedNote.getNoteTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.noteContent").value(savedNote.getNoteContent())
        );
    }

    @Test
    public void testThatDeleteNoteSuccessfullyByUserTokenAndNoteIdReturnsHttp200Ok() throws Exception {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        userEntity.setUserId(null);
        UserEntity savedUser = userService.createUser(userEntity);

        UserDto userToLogin = TestDataUtil.createTestUserDtoA();
        userToLogin.setUserId(null);
        String userJson = mapper.writeValueAsString(userToLogin);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = mapper.readTree(response);
        String token = jsonNode.get("token").asText();

        NoteEntity noteEntity = TestDataUtil.createTestNoteEntityA(savedUser);
        noteEntity.setNoteId(null);
        NoteEntity savedNote = noteService.createNote(noteEntity);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/notes/delete/"+savedNote.getNoteId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
}
