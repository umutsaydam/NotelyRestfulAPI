package com.devumut.Notely.mappers.impl;

import com.devumut.Notely.domain.dto.NotesDto;
import com.devumut.Notely.domain.entities.NoteEntity;
import com.devumut.Notely.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoteMapperImpl implements Mapper<NoteEntity, NotesDto> {

    public final ModelMapper mapper;

    @Autowired
    public NoteMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public NotesDto mapTo(NoteEntity noteEntity) {
        return mapper.map(noteEntity, NotesDto.class);
    }

    @Override
    public NoteEntity mapFrom(NotesDto notesDto) {
        return mapper.map(notesDto, NoteEntity.class);
    }
}
