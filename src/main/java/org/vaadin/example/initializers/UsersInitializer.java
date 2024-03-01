package org.vaadin.example.initializers;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.vaadin.example.entity.User;
import org.vaadin.example.entity.UserGroup;
import org.vaadin.example.repository.RoleRepository;
import org.vaadin.example.repository.UserRepository;

import java.util.Collections;

@Component
public class UsersInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UsersInitializer(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByLogin("admin") == null) {
            User user = new User();
            user.setName("Administrator");
            user.setLogin("admin");
            user.setUserGroup(new UserGroup(1L, "admin"));
            user.setPassword("$2a$10$mHBotTIqD.AOgakMUCgwIeK8Il3A/2FpvMm2nIO8QbY.9NnYTE3DW");
            user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_ADMIN")));
            user.setEmail("admin@gmail.com");
            user.setTelephone("+37312345678");
            userRepository.save(user);
        }
    }
}
