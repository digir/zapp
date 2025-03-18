package com.levelUp2.project_scaffolding_server.db.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
}
