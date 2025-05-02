package com.saintsau.slam2.gnotes30.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.saintsau.slam2.gnotes30.service.AuthService;

@Component
public class RoleInterceptor implements HandlerInterceptor {
	private final AuthService authService;

	public RoleInterceptor(AuthService authService) {
		this.authService = authService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = request.getHeader("Authorization");

		if (token == null || !token.startsWith("Bearer ")) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token manquant ou invalide");
			return false;
		}

		token = token.substring(7); // Supprimer "Bearer "

		String role = authService.getUserRoleByToken(token);
		if (role == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide");
			return false;
		}

		String requestURI = request.getRequestURI();

		// Users
		if (requestURI.startsWith("/api/users") && !(role.equals("ENSEIGNANT") || role.equals("ADMIN"))) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé : besoin du rôle ADMIN");
			return false;
		}

		if (requestURI.startsWith("/api/note") && !(role.equals("ENSEIGNANT") || role.equals("ADMIN"))) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé : besoin du rôle ENSEIGNANT ou ADMIN");
			return false;
		}

		// Filtres
		if (requestURI.startsWith("/api/etudiants ") && !(role.equals("ENSEIGNANT") || role.equals("ADMIN"))) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé : besoin du rôle ENSEIGNANT ou ADMIN");
			return false;
		}

		if (requestURI.startsWith("/api/etudiants ") && !(role.equals("ENSEIGNANT") || role.equals("ADMIN"))) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé : besoin du rôle ENSEIGNANT ou ADMIN");
			return false;
		}

		// Matieres
		if (requestURI.startsWith("/api/matieres ") && !(role.equals("ENSEIGNANT") || role.equals("ADMIN"))) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé : besoin du rôle ENSEIGNANT ou ADMIN");
			return false;
		}

		// Note
		if (requestURI.startsWith("/api/notes ") && !(role.equals("ENSEIGNANT") || role.equals("ADMIN"))) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé : besoin du rôle ENSEIGNANT ou ADMIN");
			return false;
		}

		/*
		 * if (requestURI.startsWith("/api/sam") && !(role.equals("USER") ||
		 * role.equals("Administrateur"))) {
		 * response.sendError(HttpServletResponse.SC_FORBIDDEN,
		 * "Accès refusé : besoin du rôle USER ou ADMIN"); return false; }
		 */
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// Rien à faire après la requête
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// Rien à faire après la requête
	}
}
