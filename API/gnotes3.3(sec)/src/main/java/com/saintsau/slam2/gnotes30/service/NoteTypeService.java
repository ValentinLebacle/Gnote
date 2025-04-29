package com.saintsau.slam2.gnotes30.service;

import com.saintsau.slam2.gnotes30.entity.NoteType;
import com.saintsau.slam2.gnotes30.jpaRepository.NoteTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteTypeService {

    private final NoteTypeRepository noteTypeRepository;

    public NoteTypeService(NoteTypeRepository noteTypeRepository) {
        this.noteTypeRepository = noteTypeRepository;
    }

    public List<NoteType> getAllNoteTypes() {
        return (List<NoteType>) noteTypeRepository.findAll();
    }

    public Optional<NoteType> getNoteTypeById(Integer id) {
        return noteTypeRepository.findById(id);
    }
}
