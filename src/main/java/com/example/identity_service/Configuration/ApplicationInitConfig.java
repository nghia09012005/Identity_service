package com.example.identity_service.Configuration;

import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.User;

import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.userRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

/*
* @Bean chi 1 doi tuong trong @Configuration va de spring quan ly
* co the inject no vao bat cu dau trong ung dung
*
*/


@Configuration
@RequiredArgsConstructor // lombok tao constructor va co bean trong do
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepo;

    @Bean
    ApplicationRunner applicationRunner(userRepository usRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usRepo.findByUsername("admin").isEmpty()) {
                // Lấy role từ database
                Role adminRole = roleRepo.findById("ADMIN")
                        .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
                Role userRole = roleRepo.findById("USER")
                        .orElseThrow(() -> new RuntimeException("Role USER not found"));

                HashSet<Role> roles = new HashSet<>();
                roles.add(adminRole);
                roles.add(userRole);

                User ad = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();

                usRepo.save(ad);
                log.warn("admin has been initialized:" +
                        " username: admin" +
                        " password: admin");
            }
        };
    }




}
