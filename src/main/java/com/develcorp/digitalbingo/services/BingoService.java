package com.develcorp.digitalbingo.services;

import com.develcorp.digitalbingo.repository.domain.Bingo;

import java.util.List;

public interface BingoService {

    Bingo saveGame(String username, int maxNumber,
                   List<List<Integer>> pickedNumbers,
                   List<List<Integer>> unpickedNumbers);
    Bingo updateGame(String username, int tableSize,
                     List<List<Integer>> pickedNumbers,
                     List<List<Integer>> unpickedNumbers);
    Bingo recoverLastGame(String username);

    Boolean findGameByUsername(String username);

    Bingo findLastGame();

}
