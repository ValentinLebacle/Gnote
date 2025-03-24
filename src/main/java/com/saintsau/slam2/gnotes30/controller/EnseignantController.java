package com.saintsau.slam2.gnotes30.controller;

import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.service.UserService;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
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
    public CollectionModel<EntityModel<User>> getAllEnseignants() {
        List<EntityModel<User>> enseignantModels = userService.getAllUsers().stream()
                .filter(user -> user.getRole() != null && "Enseignant".equals(user.getRole().getLibelle()))
                .map(user -> {
                    EntityModel<User> resource = EntityModel.of(user);
                    resource.add(linkTo(methodOn(EnseignantController.class).getAllEnseignants()).withRel("all-enseignants"));
                    resource.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withRel("user-details")); // 🔥 Lien vers le détail utilisateur
                    return resource;
                })
                .collect(Collectors.toList());

        return CollectionModel.of(enseignantModels, linkTo(methodOn(EnseignantController.class).getAllEnseignants()).withSelfRel());
    }
}
