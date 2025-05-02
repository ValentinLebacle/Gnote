package com.saintsau.slam2.gnotes30.service;

import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setNom("Dupont");
        user.setPrenom("Jean");
        user.setEmail("jean.dupont@example.com");
        user.setAdresse("123 rue de Paris");
        user.setTelephone("0606060606");
        user.setPasswordHash("123");
    }

    @Test
    void shouldReturnAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Dupont", result.get(0).getNom());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUserById() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Jean", result.get().getPrenom());
        verify(userRepository, times(1)).findById(1);
    }



    @Test
    void shouldCreateUser() {
        // Arrange
        User inputUser = new User();
        inputUser.setNom("Martin");
        inputUser.setPrenom("Paul");
        inputUser.setEmail("paul.martin@example.com");
        inputUser.setAdresse("456 rue Lyon");
        inputUser.setTelephone("0707070707");
        inputUser.setPasswordHash("123");

        when(userRepository.save(any(User.class))).thenReturn(inputUser);

        // Act
        User savedUser = userService.createUser(inputUser);

        // Assert
        assertNotNull(savedUser);
        assertEquals("paul.martin@example.com", savedUser.getEmail());
        assertEquals("Martin", savedUser.getNom());
        verify(userRepository, times(1)).save(any(User.class));
    }



    @Test
    void shouldUpdateUser() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setNom("Martin");
        updatedUser.setPrenom("Paul");
        updatedUser.setEmail("paul.martin@example.com");
        updatedUser.setAdresse("456 rue Lyon");
        updatedUser.setTelephone("0707070707");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.updateUser(1, updatedUser);

        // Assert
        assertEquals("Martin", result.getNom());
        assertEquals("Paul", result.getPrenom());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class, 
            () -> userService.updateUser(1, user)
        );

        assertEquals("404 NOT_FOUND \"Utilisateur non trouvé\"", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldDeleteUser() {
        // Act
        userService.deleteUser(1);

        // Assert
        verify(userRepository, times(1)).deleteById(1);
    }
}
