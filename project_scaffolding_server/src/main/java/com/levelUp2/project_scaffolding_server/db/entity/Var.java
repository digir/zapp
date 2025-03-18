package com.levelUp2.project_scaffolding_server.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "vars")
@NoArgsConstructor
@AllArgsConstructor
public class Var {
    @Id
    @ManyToOne
    @JoinColumn(name = "scaff_id", referencedColumnName = "id")
    private Scaff scaff;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "id")
    private VarType type;

    @Column(name = "\"default\"", nullable = false)
    private String defaultValue;

    @Column(nullable = false)
    private String descr;
}
