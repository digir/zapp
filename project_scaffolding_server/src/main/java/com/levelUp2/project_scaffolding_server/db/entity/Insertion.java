package com.levelUp2.project_scaffolding_server.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "insertion")
@NoArgsConstructor
@AllArgsConstructor
public class Insertion {
    @Id
    @ManyToOne
    @JoinColumn(name = "scaff_id", referencedColumnName = "id")
    private Scaff scaff;

    @Column(nullable = false)
    private String filepath;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value;
}
