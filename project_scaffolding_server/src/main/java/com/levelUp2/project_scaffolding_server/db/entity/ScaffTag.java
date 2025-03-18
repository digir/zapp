package com.levelUp2.project_scaffolding_server.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "scaff_tag")
@NoArgsConstructor
@AllArgsConstructor
public class ScaffTag {
    @Id
    @ManyToOne
    @JoinColumn(name = "scaff_id", referencedColumnName = "id")
    private Scaff scaff;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private Tag tag;
}
