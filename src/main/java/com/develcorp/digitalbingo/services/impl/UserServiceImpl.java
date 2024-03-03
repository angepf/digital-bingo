package com.develcorp.digitalbingo.services.impl;

import com.develcorp.digitalbingo.repository.UserRepository;
import com.develcorp.digitalbingo.repository.domain.User;
import com.develcorp.digitalbingo.services.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User saveUser(String username) {
        Objects.requireNonNull(username, "El nombre de usuario no puede ser nulo.");

        return userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(User.builder().username(username).build()));
    }

}