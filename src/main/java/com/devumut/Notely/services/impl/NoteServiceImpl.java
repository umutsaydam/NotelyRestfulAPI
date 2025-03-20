package com.devumut.Notely.services.impl;

import com.devumut.Notely.domain.entities.NoteEntity;
import com.devumut.Notely.repositories.NoteRepository;
import com.devumut.Notely.services.NoteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository repository;

    public NoteServiceImpl(NoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public NoteEntity createNote(NoteEntity newNote) {
        return repository.save(newNote);
    }

    @Override
    public Optional<NoteEntity> readNote(UUID noteId) {
        return repository.findById(noteId);
    }

    @Override
    public void deleteNote(UUID noteId) {
        repository.deleteById(noteId);
    }

    public NoteEntity updateNote(NoteEntity updatedNote) {
        return repository.findById(updatedNote.getNoteId()).map(existingNote -> {
            Optional.ofNullable(updatedNote.getNoteTitle()).ifPresent(existingNote::setNoteTitle);
            Optional.ofNullable(updatedNote.getNoteContent()).ifPresent(existingNote::setNoteContent);
            return repository.save(existingNote);
        }).orElseThrow(() -> new RuntimeException("Note does not exist."));
    }

    @Override
    public List<NoteEntity> getAllNotes(UUID userId) {
        return repository.getAllNotesByUserId(userId);
    }
}
