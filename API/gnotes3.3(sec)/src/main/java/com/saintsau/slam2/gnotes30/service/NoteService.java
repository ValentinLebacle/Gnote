package com.saintsau.slam2.gnotes30.service;

import com.saintsau.slam2.gnotes30.entity.Note;
import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getAllNotes() {
        return (List<Note>) noteRepository.findAll();
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public Note updateNote(Long id, Note updatedNote) {
        return noteRepository.findById(id)
                .map(note -> {
                    note.setEnseignant(updatedNote.getEnseignant());
                    note.setEleve(updatedNote.getEleve());
                    note.setMatiere(updatedNote.getMatiere());
                    note.setCoefficient(updatedNote.getCoefficient());
                    note.setValeur(updatedNote.getValeur());
                    note.setNoteType(updatedNote.getNoteType());
                    note.setCommentaire(updatedNote.getCommentaire());
                    note.setDate(updatedNote.getDate());
                    return noteRepository.save(note);
                })
                .orElseThrow(() -> new RuntimeException("Note non trouvée avec l'ID : " + id));
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
    
    public List<Note> getNotesByUser(User user) {
    	return noteRepository.findByEleveOrEnseignant(user, user);
    }

}
