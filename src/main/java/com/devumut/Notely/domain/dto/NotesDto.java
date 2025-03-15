package com.devumut.Notely.domain.dto;

import com.devumut.Notely.domain.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotesDto {
    private UUID noteId;
    private String noteTitle;
    private String noteContent;
    private UserEntity user;
}