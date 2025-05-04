package com.saintsau.slam2.gnotes30.securityTest;

import com.saintsau.slam2.gnotes30.security.RoleInterceptor;
import com.saintsau.slam2.gnotes30.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleInterceptorTest {

    private AuthService authService;
    private RoleInterceptor roleInterceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Object handler;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        roleInterceptor = new RoleInterceptor(authService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        handler = new Object();
    }

    @Test
    void testMissingToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        boolean result = roleInterceptor.preHandle(request, response, handler);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token manquant ou invalide");
        assertFalse(result);
    }

    @Test
    void testInvalidTokenFormat() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        boolean result = roleInterceptor.preHandle(request, response, handler);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token manquant ou invalide");
        assertFalse(result);
    }

    @Test
    void testTokenButNoRole() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(authService.getUserRoleByToken("validtoken")).thenReturn(null);

        boolean result = roleInterceptor.preHandle(request, response, handler);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide");
        assertFalse(result);
    }

    @Test
    void testAccessUsersWithoutAdminRole() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(authService.getUserRoleByToken("validtoken")).thenReturn("USER");
        when(request.getRequestURI()).thenReturn("/api/users");

        boolean result = roleInterceptor.preHandle(request, response, handler);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé : besoin du rôle ADMIN");
        assertFalse(result);
    }

    @Test
    void testAccessNoteWithoutRightRole() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(authService.getUserRoleByToken("validtoken")).thenReturn("USER");
        when(request.getRequestURI()).thenReturn("/api/note");

        boolean result = roleInterceptor.preHandle(request, response, handler);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé : besoin du rôle ENSEIGNANT ou ADMIN");
        assertFalse(result);
    }

    @Test
    void testAccessEtudiantsWithRightRole() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(authService.getUserRoleByToken("validtoken")).thenReturn("ADMIN");
        when(request.getRequestURI()).thenReturn("/api/etudiants");

        boolean result = roleInterceptor.preHandle(request, response, handler);

        assertTrue(result);
    }

    @Test
    void testAccessMatiereWithRightRole() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(authService.getUserRoleByToken("validtoken")).thenReturn("ENSEIGNANT");
        when(request.getRequestURI()).thenReturn("/api/matieres");

        boolean result = roleInterceptor.preHandle(request, response, handler);

        assertTrue(result);
    }

    @Test
    void testAccessNoteWithAdminRole() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(authService.getUserRoleByToken("validtoken")).thenReturn("ADMIN");
        when(request.getRequestURI()).thenReturn("/api/notes");

        boolean result = roleInterceptor.preHandle(request, response, handler);

        assertTrue(result);
    }

    @Test
    void testAccessToUnknownRoute() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(authService.getUserRoleByToken("validtoken")).thenReturn("USER");
        when(request.getRequestURI()).thenReturn("/api/anythingelse");

        boolean result = roleInterceptor.preHandle(request, response, handler);

        assertTrue(result); // Pas de restriction explicite sur cette URL
    }
}
