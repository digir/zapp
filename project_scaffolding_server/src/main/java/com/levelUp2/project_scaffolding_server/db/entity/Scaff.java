package com.levelUp2.project_scaffolding_server.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "scaffs")
@NoArgsConstructor
@AllArgsConstructor
public class Scaff {
    @Getter
    @Id
    private String id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @JsonIgnore
    private Scaff parent;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String descr;
    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "username")
    private User author;

    @Transient
    @JsonProperty("parent")
    public String getParent() {
        return parent != null ? parent.getId() : "";
    }

}
