package org.vaadin.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @ToStringExclude
    private Pos pos;
    @ManyToOne
    private IssueType type;
//    private ? subType;
//    private ? problem;
    private int priority;
    @ManyToOne
    private Status status;
    private String memo; // notes/additional info ?
    @ManyToOne
    private User owner;
    @ManyToOne
    private UserGroup assignedGroup;
    private String description;
    private LocalDateTime assignedDate;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String solution;
    @OneToMany(mappedBy = "issue", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @HashCodeExclude
    private List<Log> logs;
}
