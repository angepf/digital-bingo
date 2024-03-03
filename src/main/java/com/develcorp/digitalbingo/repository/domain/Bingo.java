package com.develcorp.digitalbingo.repository.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@Table(name = "bingo_games")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bingo {

    @Id
    @Positive
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    Long gameId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @PastOrPresent
    @Column(name = "start_time", nullable = false)
    LocalDateTime startTime;

    @Positive
    @Column(name = "table_size", nullable = false)
    Integer tableSize;

    @ElementCollection
    @CollectionTable(name = "pickedNumbers", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "pickedNumbers")
    List<List<Integer>> pickedNumbers;

    @ElementCollection
    @CollectionTable(name = "unpickedNumbers", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "unpickedNumbers")
    List<List<Integer>> unpickedNumbers;

}
