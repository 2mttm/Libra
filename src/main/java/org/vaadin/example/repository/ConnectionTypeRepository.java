package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.ConnectionType;

public interface ConnectionTypeRepository extends JpaRepository<ConnectionType, Long> {
}
