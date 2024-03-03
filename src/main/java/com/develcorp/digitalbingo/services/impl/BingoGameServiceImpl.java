package com.develcorp.digitalbingo.services.impl;

import com.develcorp.digitalbingo.repository.domain.Bingo;
import com.develcorp.digitalbingo.services.BingoService;
import com.develcorp.digitalbingo.services.BingoGameService;
import com.develcorp.digitalbingo.services.dto.PickedNumbers;
import com.develcorp.digitalbingo.services.dto.GameRecover;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BingoGameServiceImpl implements BingoGameService {

    final BingoService bingoService;
    List<List<Integer>> unpickedNumbers;
    List<List<Integer>> pickedNumbers;
    List<List<Integer>> bingoAllNumbers;
    public static int BINGO_SIZE = 5;


    private List<Integer> generateRows(Integer rowValue, Integer tableSize, boolean selected) {
        return IntStream.range(0, BINGO_SIZE)
                .mapToObj(i -> selected ? 0 : rowValue + tableSize * i)
                .collect(Collectors.toList());
    }

    private List<Integer> generateNormalRows(Integer rowValue, Integer tableSize) {
        return generateRows(rowValue, tableSize, false);
    }

    private List<Integer> generateSelectedRows() {
        return generateRows(0, 0, true);
    }

    private List<List<Integer>> generateColumns(Integer tableSize, boolean selected) {
        return IntStream.range(0, tableSize)
                .mapToObj(i -> selected ? generateSelectedRows() : generateNormalRows((i + 1) , tableSize))
                .collect(Collectors.toList());
    }

    private List<List<Integer>> generateNormalColumns(Integer tableSize) {
        return generateColumns(tableSize, false);
    }

    private List<List<Integer>> generateSelectedColumns(Integer tableSize) {
        return generateColumns(tableSize, true);
    }


    private void saveInitialGame(String username, int tableSize) {
        bingoService.saveGame(username, tableSize, pickedNumbers, unpickedNumbers);
    }

    @Override
    public List<List<Integer>> startNewGame(String username, Integer tableSize) {
        bingoAllNumbers = generateNormalColumns(tableSize);
        unpickedNumbers = generateNormalColumns(tableSize);
        pickedNumbers = generateSelectedColumns(tableSize);

        saveInitialGame(username, tableSize);

        return bingoAllNumbers;
    }

    private boolean isTableEmpty() {
        return unpickedNumbers == null || unpickedNumbers.isEmpty();
    }

    private void addSelectNumberToPickedNumbers(int number) {
        int indexRow = -1;
        int indexColumn = -1;

        for (int i = 0; i < bingoAllNumbers.size(); i++) {
            List<Integer> rowNumbers = bingoAllNumbers.get(i);
            int indexNumber = rowNumbers.indexOf(number);
            if (indexNumber != -1) {
                indexRow = i;
                indexColumn = indexNumber;
                break;
            }
        }

        List<Integer> row = pickedNumbers.get(indexRow);
        row.set(indexColumn, number);
        pickedNumbers.set(indexRow, row);
    }

    private void removePickedNumber(List<Integer> row, int rowIndex, int columnIndex) {
        row.remove(columnIndex);

        if (row.isEmpty()) {
            unpickedNumbers.remove(rowIndex);
        }
    }

    @Override
    public PickedNumbers pickedNewNumber(String username) {
        if (isTableEmpty()) {
            throw new NoSuchElementException("La tabla no tiene números para jugar.");
        }

        Random random = new Random();
        int randomRowIndex = random.nextInt(unpickedNumbers.size());
        List<Integer> row = unpickedNumbers.get(randomRowIndex);
        int randomColumnIndex = random.nextInt(row.size());
        int number = row.get(randomColumnIndex);

        addSelectNumberToPickedNumbers(number);
        removePickedNumber(row, randomRowIndex, randomColumnIndex);

        bingoService.updateGame(username, bingoAllNumbers.size(), pickedNumbers, unpickedNumbers);

        return PickedNumbers.builder()
                .actualPickedNumber(number)
                .pickedNumbers(pickedNumbers).build();
    }

    @Override
    public PickedNumbers manualPickedNewNumber(String username, Integer number) {
        if (isTableEmpty()) {
            throw new NoSuchElementException("La tabla no tiene números para jugar.");
        }

        if(number > pickedNumbers.size() * BINGO_SIZE){
            throw new NoSuchElementException("El número no está en la tabla.");
        }

        addSelectNumberToPickedNumbers(number);

        bingoService.updateGame(username, bingoAllNumbers.size(), pickedNumbers, unpickedNumbers);

        return PickedNumbers.builder()
                .actualPickedNumber(number)
                .pickedNumbers(pickedNumbers).build();
    }

    @Override
    public GameRecover recoverLastGame(String username) {
        Bingo bingo = bingoService.recoverLastGame(username);
        bingoAllNumbers = generateNormalColumns(bingo.getTableSize());
        unpickedNumbers = bingo.getUnpickedNumbers();
        printTable(unpickedNumbers, "REST");
        pickedNumbers = bingo.getPickedNumbers();
        printTable(pickedNumbers, "SELECT");

        saveInitialGame(username, bingoAllNumbers.size() * 5);

        return GameRecover.builder()
                .pickedNumbers(pickedNumbers)
                .totalNumbers(bingoAllNumbers).build();
    }

    private void printTable(List<List<Integer>> table, String title) {
        System.out.println("---------------" + title + "------------------");

        for (List<Integer> integers : table) {
            System.out.println(integers);
        }

    }

}