package com.wagnerdf.comprar.repository;

import com.wagnerdf.comprar.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUsername(String username);
    
    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.username = :username")
    void deleteAllByUsername(@Param("username") String username);
}
