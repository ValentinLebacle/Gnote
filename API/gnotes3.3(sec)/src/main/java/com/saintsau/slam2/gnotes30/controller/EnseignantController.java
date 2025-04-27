package com.saintsau.slam2.gnotes30.controller;

import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.service.UserService;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enseignants")
public class EnseignantController {

	@Autowired
	private UserService userService;

	// GET ALL Enseignants avec HATEOAS
	@GetMapping
	public List<EntityModel<User>> getAllEtudiants() {
		// Filtrer les utilisateurs avec le rôle "Élève"
		return userService.getAllUsers().stream()
				.filter(user -> user.getRole() != null && "ENSEIGNANT".equals(user.getRole().getLibelle()))
				.map(user -> {
					// Création du modèle d'entité pour chaque utilisateur
					EntityModel<User> resource = EntityModel.of(user);
					// Ajouter les liens (self et détail utilisateur)
					resource.add(linkTo(methodOn(EtudiantController.class).getAllEtudiants()).withRel("all-etudiants"));
					resource.add(
							linkTo(methodOn(UserController.class).getUserById(user.getId())).withRel("user-details"));
					return resource;
				}).collect(Collectors.toList());
	}
}
