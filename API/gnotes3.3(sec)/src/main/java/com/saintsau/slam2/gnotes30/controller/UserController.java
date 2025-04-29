package com.saintsau.slam2.gnotes30.controller;

import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.service.UserService;

import com.saintsau.slam2.gnotes30.entity.Note;
import com.saintsau.slam2.gnotes30.service.NoteService;


import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private NoteService noteService;

	// GET ALL Users
	@GetMapping
	public List<EntityModel<User>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		List<EntityModel<User>> userModels = new ArrayList<>();

		for (User user : users) {
			EntityModel<User> resource = EntityModel.of(user);
			resource.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
			userModels.add(resource);
		}

		return userModels;
	}

	// GET Etudiants BY ID
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<User>> getUserById(@PathVariable Integer id) {
		Optional<User> user = userService.getUserById(id);
		if (user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		EntityModel<User> resource = EntityModel.of(user.get());
		resource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
		resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));

		return ResponseEntity.ok(resource);
	}

	
	
	
	
	
	
	
	
	
	
	// GET Notes liées à un utilisateur (comme élève ou enseignant)
	@GetMapping("/{id}/notes")
	public ResponseEntity<List<EntityModel<Note>>> getNotesByUserId(@PathVariable Integer id) {
		Optional<User> userOpt = userService.getUserById(id);
		if (userOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		User user = userOpt.get();

		List<Note> notes = noteService.getNotesByUser(user); // méthode à créer dans ton NoteService
		List<EntityModel<Note>> noteModels = new ArrayList<>();

		for (Note note : notes) {
			EntityModel<Note> resource = EntityModel.of(note);
			resource.add(linkTo(methodOn(NoteController.class).getNoteById(note.getId())).withSelfRel());
			noteModels.add(resource);
		}

		return ResponseEntity.ok(noteModels);
	}

	
	
	
	
	
	
	
	
	
	

	// POST Création Etudiants
	@PostMapping
	public ResponseEntity<EntityModel<User>> createUser(@RequestBody User user) {
		User createdUser = userService.createUser(user);
		EntityModel<User> resource = EntityModel.of(createdUser);
		resource.add(linkTo(methodOn(UserController.class).getUserById(createdUser.getId())).withSelfRel());
		return ResponseEntity.status(HttpStatus.CREATED).body(resource);
	}

	// PUT Modification Etudiants
	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<User>> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
		try {
			User updatedUser = userService.updateUser(id, userDetails);
			EntityModel<User> resource = EntityModel.of(updatedUser);
			resource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
			return ResponseEntity.ok(resource);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// Effacer un Etudiants
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
