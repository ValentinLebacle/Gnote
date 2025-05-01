package com.saintsau.slam2.gnotes30.controller;

import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.service.MatiereAssociationService;

import java.util.ArrayList;
import java.util.List;
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

    @GetMapping("/{userId}/matieres")
    public ResponseEntity<List<EntityModel<Matiere>>> getMatieresByUser(@PathVariable Integer userId) {
        try {
            List<Matiere> matieres = matiereAssociationService.getMatieresByUser(userId);
            List<EntityModel<Matiere>> matiereModels = new ArrayList<>();

            for (Matiere matiere : matieres) {
                EntityModel<Matiere> resource = EntityModel.of(matiere);
                
                // Ajout du lien vers la matière spécifique
                resource.add(linkTo(methodOn(MatiereController.class).getMatiereById(matiere.getId())).withSelfRel());
                
                // Optionnellement, tu peux aussi ajouter un lien vers la liste de toutes les matières
                resource.add(linkTo(MatiereController.class).withRel("all-matieres"));
                
                matiereModels.add(resource);
            }

            return ResponseEntity.ok(matiereModels);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
