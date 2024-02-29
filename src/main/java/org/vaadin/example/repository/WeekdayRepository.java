package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.Weekday;

public interface WeekdayRepository extends JpaRepository<Weekday, Long> {
    Weekday findByName(String name);
}