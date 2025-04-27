package com.saintsau.slam2.gnotes30.controller;

import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.service.MatiereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/matieres")
public class MatiereController {

    @Autowired
    private MatiereService matiereService;

 // Récupérer toutes les matières
    @GetMapping
    public List<EntityModel<Matiere>> getAllMatieres() {
        List<Matiere> matieres = matiereService.getAllMatieres();
        List<EntityModel<Matiere>> matiereModels = new ArrayList<>();

        for (Matiere matiere : matieres) {
            EntityModel<Matiere> resource = EntityModel.of(matiere);
            resource.add(linkTo(methodOn(MatiereController.class).getMatiereById(matiere.getId())).withSelfRel());
            matiereModels.add(resource);
        }

        return matiereModels;
    }

    // Récupérer une matière par ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Matiere>> getMatiereById(@PathVariable Integer id) {
        Optional<Matiere> matiere = matiereService.getMatiereById(id);
        if (matiere.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        EntityModel<Matiere> resource = EntityModel.of(matiere.get());
        resource.add(linkTo(methodOn(MatiereController.class).getMatiereById(id)).withSelfRel());
        resource.add(linkTo(methodOn(MatiereController.class).getAllMatieres()).withRel("all-matieres"));

        return ResponseEntity.ok(resource);
    }

    // Créer une matière
    @PostMapping
    public ResponseEntity<EntityModel<Matiere>> createMatiere(@RequestBody Matiere matiere) {
        Matiere createdMatiere = matiereService.createMatiere(matiere);
        EntityModel<Matiere> resource = EntityModel.of(createdMatiere);
        resource.add(linkTo(methodOn(MatiereController.class).getMatiereById(createdMatiere.getId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    // Mettre à jour une matière
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Matiere>> updateMatiere(@PathVariable Integer id, @RequestBody Matiere matiereDetails) {
        try {
            Matiere updatedMatiere = matiereService.updateMatiere(id, matiereDetails);
            EntityModel<Matiere> resource = EntityModel.of(updatedMatiere);
            resource.add(linkTo(methodOn(MatiereController.class).getMatiereById(id)).withSelfRel());
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Supprimer une matière avec HATEOAS
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable Integer id) {
        matiereService.deleteMatiere(id);
        return ResponseEntity.noContent().build();
    }
}
