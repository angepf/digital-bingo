package com.develcorp.digitalbingo.services.impl;

import com.develcorp.digitalbingo.repository.BingoRepository;
import com.develcorp.digitalbingo.repository.domain.Bingo;
import com.develcorp.digitalbingo.repository.domain.User;
import com.develcorp.digitalbingo.services.BingoService;
import com.develcorp.digitalbingo.services.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BingoServiceImpl implements BingoService {

    BingoRepository bingoRepository;
    UserService userService;

    @Override
    @Transactional
    public Bingo saveGame(String username, int tableSize,
                          List<List<Integer>> pickedNumbers,
                          List<List<Integer>> unpickedNumbers) {
        Objects.requireNonNull(username, "El nombre de usuario no puede ser nulo.");

        User user = userService.saveUser(username);

        return bingoRepository.findByUser(user)
                .orElseGet(() -> bingoRepository.save(
                        Bingo.builder()
                                .user(user)
                                .tableSize(tableSize)
                                .pickedNumbers(pickedNumbers)
                                .unpickedNumbers(unpickedNumbers)
                                .startTime(LocalDateTime.now())
                                .build()));
    }

    @Override
    @Transactional
    public Bingo updateGame(String username, int tableSize,
                            List<List<Integer>> pickedNumbers,
                            List<List<Integer>> unpickedNumbers) {
        Objects.requireNonNull(username, "El nombre de usuario no puede ser nulo.");

        Optional<User> user = userService.findUserByUsername(username);

        if (user.isPresent()) {
            User userFind = user.get();

            Bingo bingo = bingoRepository.findByUser(userFind)
                    .orElseThrow(() -> new NoSuchElementException("No se encontró un juego con el usuario " + username));

            bingo.setTableSize(tableSize);
            bingo.setPickedNumbers(pickedNumbers);
            bingo.setUnpickedNumbers(unpickedNumbers);

            return bingoRepository.save(bingo);
        } else {
            throw new NoSuchElementException("no existe el usuario.");
        }
    }

    @Override
    public Bingo recoverLastGame(String username) {
        Objects.requireNonNull(username, "El nombre de usuario no puede ser nulo.");

        Optional<User> userOptional = userService.findUserByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.flatMap(bingoRepository::findByUser)
                    .orElseThrow(() -> new NoSuchElementException("No existe un juego ligado al usuario ingresado."));
        } else {
            throw new IllegalArgumentException("El usuario con nombre de usuario " + username + " no existe.");
        }
    }


    @Override
    public Boolean findGameByUsername(String username) {
        Objects.requireNonNull(username, "El nombre de usuario no puede ser nulo.");

        return userService.findUserByUsername(username)
                .map(bingoRepository::findByUser)
                .map(Optional::isPresent)
                .orElse(false);
    }

    @Override
    public Bingo findLastGame() {
        return bingoRepository.findFirstByOrderByGameIdDesc();
    }

}