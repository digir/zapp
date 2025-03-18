package com.levelUp2.project_scaffolding_server.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "substitution")
@NoArgsConstructor
@AllArgsConstructor
public class Substitution {
    @Id
    @ManyToOne
    @JoinColumn(name = "scaff_id", referencedColumnName = "id")
    private Scaff scaff;

    @Column(nullable = false)
    private String variable;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value;
}
