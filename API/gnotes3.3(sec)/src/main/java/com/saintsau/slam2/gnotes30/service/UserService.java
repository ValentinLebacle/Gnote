package com.saintsau.slam2.gnotes30.service;

import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.UserRepository;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<User> getAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	public Optional<User> getUserById(Integer id) {
		return userRepository.findById(id);
	}

	// Hachage et salage du mot de passe
	public String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(12));
	}

	public User createUser(User userDetails) {
		if (userDetails.getPasswordHash() == null || userDetails.getPasswordHash().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le mot de passe ne peut pas être vide");
		}

		User user = new User();
		user.setNom(userDetails.getNom());
		user.setPrenom(userDetails.getPrenom());
		user.setRole(userDetails.getRole());
		user.setEmail(userDetails.getEmail());
		user.setAdresse(userDetails.getAdresse());
		user.setTelephone(userDetails.getTelephone());
		user.setPasswordHash(hashPassword(userDetails.getPasswordHash()));

		return userRepository.save(user);
	}

	public User updateUser(Integer id, User userDetails) {
		return userRepository.findById(id).map(user -> {
			user.setNom(userDetails.getNom());
			user.setPrenom(userDetails.getPrenom());
			
			user.setEmail(userDetails.getEmail());
			user.setAdresse(userDetails.getAdresse());
			user.setTelephone(userDetails.getTelephone());
			return userRepository.save(user);
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
	}

	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}
}
