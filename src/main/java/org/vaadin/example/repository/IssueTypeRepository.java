package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.IssueType;

public interface IssueTypeRepository extends JpaRepository<IssueType, Long> {
}
