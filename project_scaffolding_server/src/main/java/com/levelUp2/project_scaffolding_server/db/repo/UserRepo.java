package com.levelUp2.project_scaffolding_server.db.repo;

import com.levelUp2.project_scaffolding_server.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
}