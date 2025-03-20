package com.levelUp2.project_scaffolding_server.db.repo;


import com.levelUp2.project_scaffolding_server.db.entity.Scaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScaffRepo extends JpaRepository<Scaff, String> {
    List<Scaff> findByParentId(String parentId);
    Optional<Scaff> findByName(String name);
}