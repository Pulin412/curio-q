package com.app.curioq.authservice.repository;

import com.app.curioq.authservice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByUserId(String userId);
}
