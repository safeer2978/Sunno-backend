package com.sunno.accountservice.models.persistence.repository;

import com.sunno.accountservice.models.persistence.JwtRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Integer> {

    @Query("FROM JwtRefreshToken WHERE token = :token")
    Optional<JwtRefreshToken> findByToken(@Param("token") String Token);
}