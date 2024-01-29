package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.City;

public interface CityRepository extends JpaRepository<City, Long> {
}
