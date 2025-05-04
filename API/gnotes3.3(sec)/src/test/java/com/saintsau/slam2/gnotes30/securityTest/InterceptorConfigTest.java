package com.saintsau.slam2.gnotes30.securityTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.saintsau.slam2.gnotes30.security.InterceptorConfig;
import com.saintsau.slam2.gnotes30.security.RoleInterceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistration;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterceptorConfigTest {

    @Mock
    private RoleInterceptor roleInterceptor;

    @Mock
    private InterceptorRegistry interceptorRegistry;

    @Mock
    private InterceptorRegistration interceptorRegistration;

    @InjectMocks
    private InterceptorConfig interceptorConfig;

    @Test
    public void testAddInterceptors() {
        // Simuler le comportement de addInterceptor pour qu'il retourne interceptorRegistration
        when(interceptorRegistry.addInterceptor(roleInterceptor)).thenReturn(interceptorRegistration);
        when(interceptorRegistration.addPathPatterns("/api/**")).thenReturn(interceptorRegistration);
        when(interceptorRegistration.excludePathPatterns("/api/auth/**")).thenReturn(interceptorRegistration);

        // Appeler la méthode à tester
        interceptorConfig.addInterceptors(interceptorRegistry);

        // Vérifier les appels
        verify(interceptorRegistry).addInterceptor(roleInterceptor);
        verify(interceptorRegistration).addPathPatterns("/api/**");
        verify(interceptorRegistration).excludePathPatterns("/api/auth/**");
    }
}
