package dev.leocamacho.authentication.handlers;

import dev.leocamacho.authentication.exceptions.BusinessException;
import dev.leocamacho.authentication.handlers.commands.AuthenticationHandler;
import dev.leocamacho.authentication.handlers.queries.UserAuthenticationQuery;
import dev.leocamacho.authentication.http.JwtService;
import dev.leocamacho.authentication.models.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthenticationHandlerTest {

    @Mock
    private UserAuthenticationQuery userAuthenticationQuery;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationHandler authenticationHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    // Test the authenticateWithJwt method

    @Test
    void testAuthenticateWithJwt() {
        String username = "testUser";
        String password = "testPassword";
        String encodedPassword = "encodedPassword";
        Collection<String> authorities = Arrays.asList("ROLE_USER");
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(UUID.randomUUID(), username, encodedPassword, authorities);

        when(userAuthenticationQuery.loadUserByUsername(username)).thenReturn(authenticatedUser);
        when(passwordEncoder.matches(password, authenticatedUser.password())).thenReturn(true);
        when(jwtService.generateToken(authenticatedUser)).thenReturn("generatedToken");

        String token = authenticationHandler.authenticateWithJwt(username, password);

        assertEquals("generatedToken", token);
    }

    // Test the authenticate method with invalid user and password

    @Test
    void testAuthenticateWithInvalidUser() {
        String username = "invalidUser";
        String password = "testPassword";

        when(userAuthenticationQuery.loadUserByUsername(username)).thenReturn(null);

        assertThrows(BusinessException.class, () -> authenticationHandler.authenticateWithJwt(username, password));
    }

    // Test the authenticate method with invalid password

    @Test
    void testAuthenticateWithInvalidPassword() {
        String username = "testUser";
        String password = "invalidPassword";
        String encodedPassword = "encodedPassword";
        Collection<String> authorities = Arrays.asList("ROLE_USER");
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(UUID.randomUUID(), username, encodedPassword, authorities);

        when(userAuthenticationQuery.loadUserByUsername(username)).thenReturn(authenticatedUser);
        when(passwordEncoder.matches(password, authenticatedUser.password())).thenReturn(false);

        assertThrows(BusinessException.class, () -> authenticationHandler.authenticateWithJwt(username, password));
    }
}
