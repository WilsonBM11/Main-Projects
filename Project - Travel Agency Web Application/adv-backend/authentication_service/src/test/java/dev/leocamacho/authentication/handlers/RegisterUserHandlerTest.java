package dev.leocamacho.authentication.handlers;

import dev.leocamacho.authentication.exceptions.BusinessException;
import dev.leocamacho.authentication.exceptions.InvalidInputException;
import dev.leocamacho.authentication.handlers.commands.RegisterUserHandler;
import dev.leocamacho.authentication.jpa.entities.UserEntity;
import dev.leocamacho.authentication.jpa.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegisterUserHandlerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private RegisterUserHandler registerUserHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test the register method with a new user and valid data
    @Test
    void testRegisterNewUser() {
        RegisterUserHandler.Command command = new RegisterUserHandler.Command("test@example.com", "Test User", "password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        registerUserHandler.register(command);

        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(jmsTemplate, times(1)).convertAndSend(eq("message"), anyString());
        verify(jmsTemplate, times(1)).convertAndSend(eq("subject"), eq("Registro exitoso"));
        verify(jmsTemplate, times(1)).convertAndSend(eq("toUser"),  eq("test@example.com"));
    }

    // Test the register method with an existing user
    @Test
    void testRegisterExistingUser() {
        RegisterUserHandler.Command command = new RegisterUserHandler.Command("existing@example.com", "Existing User", "password");

        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(new UserEntity()));

        assertThrows(BusinessException.class, () -> registerUserHandler.register(command));
    }

    // Test the register method with missing fields

    @Test
    void testRegisterWithMissingFields() {
        RegisterUserHandler.Command command = new RegisterUserHandler.Command("", "", "");

        assertThrows(InvalidInputException.class, () -> registerUserHandler.register(command));
    }
}
