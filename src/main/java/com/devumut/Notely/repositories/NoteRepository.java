package com.devumut.Notely.repositories;

import com.devumut.Notely.domain.entities.NoteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteRepository extends CrudRepository<NoteEntity, UUID> {

    @Query("SELECT n FROM NoteEntity n WHERE n.user.userId = ?1")
    List<NoteEntity> getAllNotesByUserId(UUID userId);
}
