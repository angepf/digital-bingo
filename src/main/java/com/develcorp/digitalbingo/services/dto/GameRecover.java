package com.develcorp.digitalbingo.services.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameRecover {

    List<List<Integer>> pickedNumbers;
    List<List<Integer>> totalNumbers;

}
