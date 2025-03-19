package com.levelUp2.project_scaffolding_server.db.service;

import com.levelUp2.project_scaffolding_server.db.entity.User;
import com.levelUp2.project_scaffolding_server.db.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {
    private final UserRepo userRepository;

    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
