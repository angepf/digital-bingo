package com.develcorp.digitalbingo.services;

import com.develcorp.digitalbingo.services.dto.PickedNumbers;
import com.develcorp.digitalbingo.services.dto.GameRecover;

import java.util.List;

public interface BingoGameService {

    List<List<Integer>> startNewGame(String username, Integer maxNumber);

    GameRecover recoverLastGame(String username);

    PickedNumbers pickedNewNumber(String username);

    PickedNumbers manualPickedNewNumber(String username, Integer number);
}
