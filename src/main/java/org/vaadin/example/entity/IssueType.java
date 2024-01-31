package org.vaadin.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class IssueType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int issueLevel;
//    @ManyToOne
//    private Issue parentIssue;
    private String name;
    private LocalDateTime insertDate;

    @Override
    public String toString(){
        return this.name;
    }
}
