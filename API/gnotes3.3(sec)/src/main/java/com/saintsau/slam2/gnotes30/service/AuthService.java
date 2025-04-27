package com.saintsau.slam2.gnotes30.service;

import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final Map<String, String> tokenStore = new HashMap<>(); // Stocke les tokens en mémoire
	private final Map<String, Long> tokenExpiration = new HashMap<>(); // Stocke les dates d'expiration des tokens

	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Hachage et salage du mot de passe
	public String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(12));
	}

	// Enregistrement d'un utilisateur
	public User registerUser(String email, String password) {
		String hashedPassword = hashPassword(password);
		User newUser = new User(email, hashedPassword);
		return userRepository.save(newUser);
	}

	// Authentification et génération de token
	public String login(String email, String password) {
		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isEmpty() || !BCrypt.checkpw(password, userOpt.get().getPasswordHash())) {
			return null; // Mauvais email ou mot de passe
		}

		// Supprimer les anciens tokens pour cet utilisateur
		tokenStore.entrySet().removeIf(entry -> entry.getValue().equals(email));

		// Générer un nouveau token
		String token = UUID.randomUUID().toString();

		// Ajouter une expiration (par exemple, 30 minutes)
		long expirationTime = System.currentTimeMillis() + (30 * 60 * 1000); // 30 minutes
		tokenStore.put(token, email); // Stocker le token
		tokenExpiration.put(token, expirationTime); // Ajouter la date d'expiration

		return token;
	}

	// Vérification du token
	public boolean isAuthenticated(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7); // Retirer le préfixe "Bearer "
		}

		if (tokenStore.containsKey(token)) {
			long expirationTime = tokenExpiration.get(token);
			if (System.currentTimeMillis() > expirationTime) {
				tokenStore.remove(token); // Supprimer le token expiré
				tokenExpiration.remove(token);
				return false; // Token expiré
			}
			return true; // Token valide
		}
		return false; // Token non trouvé
	}

	// Récupérer l'utilisateur à partir du token
	public Optional<User> getUserByToken(String token) {
		String email = tokenStore.get(token);
		return email != null ? userRepository.findByEmail(email) : Optional.empty();
	}

	// Déconnexion : suppression du token
	public boolean logout(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7); // Retirer le préfixe "Bearer "
		}

		if (tokenStore.containsKey(token)) {
			tokenStore.remove(token);
			tokenExpiration.remove(token);
			return true; // Token supprimé avec succès
		}
		return false; // Token non trouvé ou déjà expiré
	}

	// Récupérer le rôle de l'utilisateur à partir du token
	public String getUserRoleByToken(String token) {
	    if (token != null && token.startsWith("Bearer ")) {
	        token = token.substring(7); // Retirer le préfixe "Bearer "
	    }

	    if (tokenStore.containsKey(token)) {
	        String email = tokenStore.get(token);
	        Optional<User> userOpt = userRepository.findByEmail(email);

	        	return userOpt.map(user -> user.getRole().getLibelle()).orElse(null);
	    }
	    return null;
	}


}
