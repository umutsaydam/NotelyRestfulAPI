package com.devumut.Notely.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notes")
public class NotesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID noteId;

    @Column(name = "note_title")
    private String noteTitle;

    @Column(name = "note_content")
    private String noteContent;

    @ManyToOne
    @JoinColumn(name = "user_note_id")
    private UserEntity user;
}
