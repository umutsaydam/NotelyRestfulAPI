package com.devumut.Notely.controllers;

import com.devumut.Notely.domain.dto.NotesDto;
import com.devumut.Notely.domain.entities.NoteEntity;
import com.devumut.Notely.domain.entities.UserEntity;
import com.devumut.Notely.jwt.JwtUtil;
import com.devumut.Notely.mappers.Mapper;
import com.devumut.Notely.services.NoteService;
import com.devumut.Notely.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private final NoteService noteService;
    private final Mapper<NoteEntity, NotesDto> noteMapper;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    public NotesController(NoteService noteService, Mapper<NoteEntity, NotesDto> noteMapper, JwtUtil jwtUtil, TokenService tokenService) {
        this.noteService = noteService;
        this.noteMapper = noteMapper;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<NotesDto> createNote(
            @RequestHeader("Authorization") String token,
            @RequestBody NotesDto notesDto
    ) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (!tokenService.isTokenValid(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (notesDto.getNoteContent().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UUID userId = jwtUtil.extractUserId(token);
        UserEntity userRef = new UserEntity();
        userRef.setUserId(userId);
        if (notesDto.getNoteContent() == null || notesDto.getNoteContent().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        NoteEntity noteEntity = noteMapper.mapFrom(notesDto);
        noteEntity.setUser(userRef);
        NoteEntity savedNoteEntity = noteService.createNote(noteEntity);
        savedNoteEntity.setUser(null);
        return new ResponseEntity<>(noteMapper.mapTo(savedNoteEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/read/{note_id}")
    public ResponseEntity<NotesDto> readNote(
            @RequestHeader("Authorization") String token,
            @PathVariable("note_id") UUID noteId
    ) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (!tokenService.isTokenValid(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<NoteEntity> optionalNote = noteService.readNote(noteId);
        return optionalNote.map(noteEntity ->
                new ResponseEntity<>(noteMapper.mapTo(noteEntity), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/delete/{note_id}")
    public ResponseEntity<HttpStatus> deleteNoteById(
            @RequestHeader("Authorization") String token,
            @PathVariable("note_id") UUID noteId) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (!tokenService.isTokenValid(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        noteService.deleteNote(noteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/update")
    public ResponseEntity<NotesDto> updateNote(
            @RequestHeader("Authorization") String token,
            @RequestBody NotesDto notesDto) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (!tokenService.isTokenValid(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        NoteEntity noteEntity = noteMapper.mapFrom(notesDto);
        try {
            NoteEntity updatedNote = noteService.updateNote(noteEntity);
            return new ResponseEntity<>(noteMapper.mapTo(updatedNote), HttpStatus.OK);
        } catch (RuntimeException e) {
            // e.getMessage();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
