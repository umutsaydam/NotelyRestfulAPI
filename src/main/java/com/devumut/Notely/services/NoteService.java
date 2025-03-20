package com.devumut.Notely.services;

import com.devumut.Notely.domain.entities.NoteEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteService {
    NoteEntity createNote(NoteEntity newNote);
    Optional<NoteEntity> readNote(UUID noteId);
    void deleteNote(UUID noteId);
    NoteEntity updateNote(NoteEntity updatedNote);
    List<NoteEntity> getAllNotes(UUID userId);
}
