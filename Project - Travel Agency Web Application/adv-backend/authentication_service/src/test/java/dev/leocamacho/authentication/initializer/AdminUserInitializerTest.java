package dev.leocamacho.authentication.initializer;

import dev.leocamacho.authentication.jpa.entities.UserEntity;
import dev.leocamacho.authentication.jpa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminUserInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminUserInitializer adminUserInitializer;

    // Test the createAdminUser method when no admin user exists
    @Test
    public void testCreateAdminUser_WhenNoAdminUserExists() {
        when(userRepository.findByRoles("ADMIN")).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode("admin")).thenReturn("encodedPassword");

        adminUserInitializer.createAdminUser();

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    // Test the createAdminUser method when an admin user exists
    @Test
    public void testCreateAdminUser_WhenAdminUserExists() {
        // Arrange
        UserEntity existingAdminUser = new UserEntity();
        existingAdminUser.setEmail("admin@gmail.com");
        existingAdminUser.setName("admin");
        existingAdminUser.setPassword("existingEncodedPassword");
        existingAdminUser.setRoles(Collections.singletonList("ADMIN"));

        when(userRepository.findByRoles("ADMIN")).thenReturn(Collections.singletonList(existingAdminUser));
        
        adminUserInitializer.createAdminUser();

        verify(userRepository, never()).save(any(UserEntity.class));
    }
}


