package com.app.curioq.authservice.authservice.repository;

import com.app.curioq.authservice.authservice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
//    @Query(value = """
//            select t from Token t inner join User u\s
//            on t.user.id = u.id\s
//            where u.id = :id and (t.expired = false or t.revoked = false)\s
//            """)
//    List<Token> findAllValidTokenByUser(Integer id);

    Optional<Token> findByToken(String token);
    Optional<Token> findByUserId(String userId);
}
