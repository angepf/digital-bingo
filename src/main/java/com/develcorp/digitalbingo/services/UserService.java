package com.develcorp.digitalbingo.services;

import com.develcorp.digitalbingo.repository.domain.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByUsername(String username);
    User saveUser(String username);

}
