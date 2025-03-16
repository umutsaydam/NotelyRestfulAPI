package com.devumut.Notely.repositories;

import com.devumut.Notely.domain.entities.NoteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoteRepository extends CrudRepository<NoteEntity, UUID> {
}
