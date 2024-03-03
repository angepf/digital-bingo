package com.develcorp.digitalbingo.controller;

import com.develcorp.digitalbingo.services.BingoGameService;
import com.develcorp.digitalbingo.services.dto.PickedNumbers;
import com.develcorp.digitalbingo.services.dto.GameRecover;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BingoController {

    BingoGameService bingoGameService;

    @GetMapping("/startNewGame/{bingoTableSize}")
    public ResponseEntity<List<List<Integer>>> startNewGame(@RequestHeader String username,
                                                            @PathVariable Integer bingoTableSize) {
        return ResponseEntity.ok(bingoGameService.startNewGame(username, bingoTableSize));
    }

    @GetMapping("/pickedNewNumber")
    public ResponseEntity<PickedNumbers> pickedNewNumber(@RequestHeader String username) {
        return ResponseEntity.ok(bingoGameService.pickedNewNumber(username));
    }

    @GetMapping("/manualPickedNewNumber/{number}")
    public ResponseEntity<PickedNumbers> manualPickedNewNumber(@RequestHeader String username,
                                                               @PathVariable Integer number) {
        return ResponseEntity.ok(bingoGameService.manualPickedNewNumber(username, number));
    }

    @GetMapping("/recoverLastGame")
    public ResponseEntity<GameRecover> recoverLastGame(@RequestHeader String username) {
        return ResponseEntity.ok(bingoGameService.recoverLastGame(username));
    }

}
