package com.levelUp2.project_scaffolding_server;

import com.levelUp2.project_scaffolding_server.db.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppInitializer {

    private final UserService userService;

    @Autowired
    public AppInitializer(UserService userService) {
        this.userService = userService;
        AuthenticateUser.setUserService(userService);
    }
}