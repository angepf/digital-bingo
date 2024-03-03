package com.develcorp.digitalbingo.repository;

import com.develcorp.digitalbingo.repository.domain.Bingo;
import com.develcorp.digitalbingo.repository.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpAnd;

import java.util.Optional;

public interface BingoRepository extends JpaRepository<Bingo, Long> {

    Optional<Bingo> findByUser(User user);
    Optional<Bingo> findByGameId(Long gameId);
    Bingo findFirstByOrderByGameIdDesc();
}
