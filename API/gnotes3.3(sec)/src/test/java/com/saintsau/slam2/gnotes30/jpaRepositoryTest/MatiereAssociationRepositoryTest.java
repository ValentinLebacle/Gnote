package com.saintsau.slam2.gnotes30.jpaRepositoryTest;

import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.entity.MatiereAssociation;
import com.saintsau.slam2.gnotes30.entity.MatiereAssociationId;
import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.MatiereAssociationRepository;
import com.saintsau.slam2.gnotes30.jpaRepository.MatiereRepository;
import com.saintsau.slam2.gnotes30.jpaRepository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MatiereAssociationRepositoryTest {

    @Autowired
    private MatiereAssociationRepository repository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Matiere matiere;

    @BeforeEach
    void setUp() {
        // Création de l'utilisateur
        user = new User();
        user.setNom("Doe");
        user.setPrenom("John");
        user.setEmail("john.doe@example.com");
        user.setPasswordHash("securepassword123");
        user = userRepository.save(user);

        // Création de la matière
        matiere = new Matiere();
        matiere.setLibelle("Mathématiques");
        matiere = matiereRepository.save(matiere);
    }


    @Test
    @Transactional  // Ajouter cette annotation pour garantir la gestion des transactions
    void testFindMatieresByUserId() {
        // Création de la clé composite
        MatiereAssociationId id = new MatiereAssociationId();
        id.setUserId(user.getId());
        id.setMatId(matiere.getId());

        // Création et sauvegarde de l'association
        MatiereAssociation matiereAssociation = new MatiereAssociation();
        matiereAssociation.setId(id);
        matiereAssociation.setUser(user);
        matiereAssociation.setMatiere(matiere);

        repository.save(matiereAssociation);

        // Test de la méthode
        List<Matiere> matieres = repository.findByUserId(user.getId())
                                           .stream()
                                           .map(MatiereAssociation::getMatiere)
                                           .collect(Collectors.toList());

        assertEquals(1, matieres.size());
        assertEquals(matiere.getId(), matieres.get(0).getId());
    }

    @Test
    @Transactional  // Ajouter cette annotation pour garantir la gestion des transactions
    void testFindMatieresByUserIdNoResults() {
        List<Matiere> matieres = repository.findByUserId(999)
                                           .stream()
                                           .map(MatiereAssociation::getMatiere)
                                           .collect(Collectors.toList());

        assertEquals(0, matieres.size());
    }
}
