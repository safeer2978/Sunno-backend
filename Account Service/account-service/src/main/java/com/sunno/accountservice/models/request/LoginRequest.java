package com.sunno.accountservice.models.request;

import lombok.Data;

@Data
public class LoginRequest {
    String email;
    String password;
}
