package com.levelUp2.project_scaffolding_server.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "insertion")
@NoArgsConstructor
@AllArgsConstructor
public class Insertion {
    @Getter
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scaff_id", referencedColumnName = "id")
    private Scaff scaff;

    @Column(nullable = false)
    private String filepath;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value;
}
