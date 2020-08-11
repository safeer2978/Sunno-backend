package com.sunno.accountservice.models.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtAuthenticationResponse implements Serializable {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresInMsec;

    public JwtAuthenticationResponse(String accessToken, String refreshToken, Long expiresInMsec) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresInMsec = expiresInMsec;
    }
}
