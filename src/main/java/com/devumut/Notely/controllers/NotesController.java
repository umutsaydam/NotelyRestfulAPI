package com.devumut.Notely.controllers;

import com.devumut.Notely.domain.dto.NotesDto;
import com.devumut.Notely.domain.entities.NoteEntity;
import com.devumut.Notely.mappers.Mapper;
import com.devumut.Notely.services.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private final NoteService service;
    private final Mapper<NoteEntity, NotesDto> noteMapper;

    public NotesController(NoteService service, Mapper<NoteEntity, NotesDto> noteMapper) {
        this.service = service;
        this.noteMapper = noteMapper;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<NotesDto> createNote(@RequestBody NotesDto notesDto) {
        if (notesDto.getNoteContent().isEmpty()) {
            // note content can not be null
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        NoteEntity noteEntity = noteMapper.mapFrom(notesDto);
        NoteEntity savedNoteEntity = service.createNote(noteEntity);
        return new ResponseEntity<>(noteMapper.mapTo(savedNoteEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/read/{note_id}")
    public ResponseEntity<NotesDto> readNote(@PathVariable("note_id") UUID noteId) {
        Optional<NoteEntity> optionalNote = service.readNote(noteId);
        return optionalNote.map(noteEntity -> new ResponseEntity<>(noteMapper.mapTo(noteEntity), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/delete/{note_id}")
    public ResponseEntity<HttpStatus> deleteNoteById(@PathVariable("note_id") UUID noteId) {
        service.deleteNote(noteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/update")
    public ResponseEntity<NotesDto> updateNote(@RequestBody NotesDto notesDto) {
        NoteEntity noteEntity = noteMapper.mapFrom(notesDto);
        try {
            NoteEntity updatedNote = service.updateNote(noteEntity);
            return new ResponseEntity<>(noteMapper.mapTo(updatedNote), HttpStatus.OK);
        } catch (RuntimeException e) {
            // e.getMessage();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
