package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByName(String status);
}
