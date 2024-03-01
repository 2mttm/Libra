package org.vaadin.example.initializers;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.vaadin.example.entity.UserGroup;
import org.vaadin.example.repository.UserGroupRepository;

@Component
public class UserGroupInitializer implements ApplicationRunner {
    private final UserGroupRepository userGroupRepository;

    public UserGroupInitializer(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userGroupRepository.findByName("admin") == null) {
            UserGroup userGroup = new UserGroup();
            userGroup.setName("admin");
            userGroupRepository.save(userGroup);
        }
        if (userGroupRepository.findByName("technical group") == null) {
            UserGroup userGroup = new UserGroup();
            userGroup.setName("technical group");
            userGroupRepository.save(userGroup);
        }
        if (userGroupRepository.findByName("office worker") == null) {
            UserGroup userGroup = new UserGroup();
            userGroup.setName("office worker");
            userGroupRepository.save(userGroup);
        }
    }
}