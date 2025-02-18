package dev.leocamacho.authentication.initializer;

import dev.leocamacho.authentication.jpa.entities.UserEntity;
import dev.leocamacho.authentication.jpa.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AdminUserInitializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

   @PostConstruct
   public void createAdminUser(){
       if(userRepository.findByRoles("ADMIN").isEmpty()) {
           UserEntity adminUser = new UserEntity();
           adminUser.setEmail("admin@gmail.com");
           adminUser.setName("admin");
           adminUser.setPassword(passwordEncoder.encode("admin"));
           adminUser.setRoles(Collections.singletonList("ADMIN"));

           userRepository.save(adminUser);

       }
    }
}
