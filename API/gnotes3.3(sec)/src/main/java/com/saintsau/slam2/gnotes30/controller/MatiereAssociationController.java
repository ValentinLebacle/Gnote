package com.saintsau.slam2.gnotes30.controller;

import com.saintsau.slam2.gnotes30.service.MatiereAssociationService;

import java.util.Map;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping("/api/users")
public class MatiereAssociationController {

    private final MatiereAssociationService matiereAssociationService;

    public MatiereAssociationController(MatiereAssociationService matiereAssociationService) {
        this.matiereAssociationService = matiereAssociationService;
    }

    @PostMapping("/{userId}/matieres/{matId}")
    public ResponseEntity<EntityModel<Map<String, String>>> associerMatiereAUser(@PathVariable Integer userId, @PathVariable Integer matId) {
        try {
            matiereAssociationService.associerMatiereAUser(userId, matId);

            Map<String, String> response = Map.of("message", "Matière associée avec succès !");
            EntityModel<Map<String, String>> resource = EntityModel.of(response);
            resource.add(linkTo(methodOn(MatiereAssociationController.class).associerMatiereAUser(userId, matId)).withSelfRel());
            resource.add(linkTo(methodOn(MatiereAssociationController.class).supprimerAssociation(userId, matId)).withRel("supprimer-association"));

            return ResponseEntity.status(HttpStatus.CREATED).body(resource);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = Map.of("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EntityModel.of(errorResponse));
        }
    }

    @DeleteMapping("/{userId}/matieres/{matId}")
    public ResponseEntity<EntityModel<Map<String, String>>> supprimerAssociation(@PathVariable Integer userId, @PathVariable Integer matId) {
        try {
            matiereAssociationService.supprimerAssociation(userId, matId);

            Map<String, String> response = Map.of("message", "Association supprimée avec succès !");
            EntityModel<Map<String, String>> resource = EntityModel.of(response);
            resource.add(linkTo(methodOn(MatiereAssociationController.class).associerMatiereAUser(userId, matId)).withRel("associer-matiere"));

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(resource);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = Map.of("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EntityModel.of(errorResponse));
        }
    }
}
