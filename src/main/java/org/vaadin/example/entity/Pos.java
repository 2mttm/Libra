package org.vaadin.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pos { // Point of Sale
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String telephone;
    private String cellphone;
    private String address;
    @ManyToOne
    private City city;
    private String model;
    private String brand;
    @ManyToOne
    private ConnectionType connectionType;
    private LocalTime morningOpening;
    private LocalTime morningClosing;
    private LocalTime afternoonOpening;
    private LocalTime afternoonClosing;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Weekday> closedDays;
    private LocalDateTime insertDate;
    @OneToMany(mappedBy = "pos", fetch = FetchType.EAGER)
    @HashCodeExclude
    private List<Issue> issues;
}
