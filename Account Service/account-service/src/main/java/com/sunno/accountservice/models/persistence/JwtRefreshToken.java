package com.sunno.accountservice.models.persistence;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@Table(name = "refresh_tokens")
public class JwtRefreshToken {
    @Id
    @Column
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    private int id;

    private String token;

    public JwtRefreshToken(){}

    public JwtRefreshToken(String token) {
        this.token = token;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Instant expirationDateTime;

    public JwtRefreshToken(JwtRefreshToken jwtRefreshToken) {
        this.token = jwtRefreshToken.token;
        this.user = jwtRefreshToken.user;
        this.expirationDateTime = jwtRefreshToken.expirationDateTime;
    }
}