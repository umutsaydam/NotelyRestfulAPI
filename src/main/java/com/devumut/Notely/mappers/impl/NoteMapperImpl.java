package com.devumut.Notely.mappers.impl;

import com.devumut.Notely.domain.dto.NotesDto;
import com.devumut.Notely.domain.entities.NotesEntity;
import com.devumut.Notely.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NoteMapperImpl implements Mapper<NotesEntity, NotesDto> {

    public final ModelMapper mapper;

    @Autowired
    public NoteMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public NotesDto mapTo(NotesEntity notesEntity) {
        return mapper.map(notesEntity, NotesDto.class);
    }

    @Override
    public NotesEntity mapFrom(NotesDto notesDto) {
        return mapper.map(notesDto, NotesEntity.class);
    }
}
