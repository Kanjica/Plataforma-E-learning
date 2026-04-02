package com.lp3.elearning.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lp3.elearning.entities.User;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.repository.UserRepository;

@Component
@Profile("local")
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Override
    public void run(String... args) throws Exception{
        if(adminUsername == null || adminUsername.isEmpty()){
            return;
        }

        if(userRepository.findByUsername(adminUsername).isEmpty()){
            User admin = User.builder()
                .name(adminUsername) 
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(UserRole.ROLE_ADMIN)
                .build();

            userRepository.save(admin);
            System.out.println(">>>> SEEDER: Usuário Admin [" + adminUsername + "] criado para ambiente local.");
        }
    }
}