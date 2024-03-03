package com.develcorp.digitalbingo.services.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PickedNumbers {

    Integer actualPickedNumber;
    List<List<Integer>> pickedNumbers;

}
