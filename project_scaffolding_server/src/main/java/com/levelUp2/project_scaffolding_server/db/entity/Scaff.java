package com.levelUp2.project_scaffolding_server.db.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "scaffs")
@NoArgsConstructor
@AllArgsConstructor
public class Scaff {
    @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    //// @JsonBackReference // Prevent infinite recursion when serializing self-referential field
    @JsonIgnore
    private Scaff parent;

    @Transient
    @JsonProperty("parent")
    public String getParent() {
        return parent != null ? parent.getId() : "";
    }

    //// @OneToMany(mappedBy = "parent")
    //// @JsonManagedReference // Enable serialization of children
    //// private List<Scaff> children;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String descr;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "username")
    private User author;
}
