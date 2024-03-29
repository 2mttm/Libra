package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
