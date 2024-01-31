package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.example.entity.Issue;
import org.vaadin.example.entity.Status;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    @Query("select i from Issue i " +
            "where lower(i.memo) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(i.description) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(i.solution) like lower(concat('%', :searchTerm, '%'))")
    List<Issue> search(@Param("searchTerm") String searchTerm);

    List<Issue> findAllByStatus(Status status);
}
