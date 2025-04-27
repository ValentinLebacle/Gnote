package com.saintsau.slam2.gnotes30.controller;

import com.saintsau.slam2.gnotes30.entity.Note;
import com.saintsau.slam2.gnotes30.service.NoteService;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

	private final NoteService noteService;

	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}

	// Récupérer toutes les notes
	@GetMapping
	public List<EntityModel<Note>> getAllNotes() {
		List<Note> notes = noteService.getAllNotes();
		List<EntityModel<Note>> noteModels = new ArrayList<>();

		for (Note note : notes) {
			EntityModel<Note> resource = EntityModel.of(note);
			resource.add(linkTo(methodOn(NoteController.class).getNoteById(note.getId())).withSelfRel());
			noteModels.add(resource);
		}

		return noteModels;
	}

	// Récupérer une note par ID
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Note>> getNoteById(@PathVariable Long id) {
		Optional<Note> note = noteService.getNoteById(id);
		if (note.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		EntityModel<Note> resource = EntityModel.of(note.get());
		resource.add(linkTo(methodOn(NoteController.class).getNoteById(id)).withSelfRel());
		resource.add(linkTo(methodOn(NoteController.class).getAllNotes()).withRel("all-notes"));

		return ResponseEntity.ok(resource);
	}

	// Ajouter une nouvelle note
	@PostMapping
	public ResponseEntity<EntityModel<Note>> createNote(@RequestBody Note note) {
		Note createdNote = noteService.createNote(note);
		EntityModel<Note> resource = EntityModel.of(createdNote);
		resource.add(linkTo(methodOn(NoteController.class).getNoteById(createdNote.getId())).withSelfRel());
		return ResponseEntity.status(HttpStatus.CREATED).body(resource);
	}

	// Mettre à jour une note existante
	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<Note>> updateNote(@PathVariable Long id, @RequestBody Note updatedNote) {
		try {
			Note updated = noteService.updateNote(id, updatedNote);
			EntityModel<Note> resource = EntityModel.of(updated);
			resource.add(linkTo(methodOn(NoteController.class).getNoteById(id)).withSelfRel());
			return ResponseEntity.ok(resource);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// Supprimer une note
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
		noteService.deleteNote(id);
		return ResponseEntity.noContent().build();
	}
}
