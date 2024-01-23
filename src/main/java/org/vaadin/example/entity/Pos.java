package org.vaadin.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pos {
    @Id
    @GeneratedValue
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
    private LocalDateTime morningOpening;
    private LocalDateTime morningClosing;
    private LocalDateTime afternoonOpening;
    private LocalDateTime afternoonClosing;
    @Column(columnDefinition = "SET('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')")
    private String closedDays;
    private LocalDateTime insertDate;
}
