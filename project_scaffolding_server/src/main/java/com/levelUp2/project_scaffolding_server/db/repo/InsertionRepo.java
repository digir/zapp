package com.levelUp2.project_scaffolding_server.db.repo;


import com.levelUp2.project_scaffolding_server.db.entity.Insertion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InsertionRepo extends JpaRepository<Insertion, String> {
}