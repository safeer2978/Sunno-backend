package com.sunno.accountservice.models.request;

import lombok.Data;

@Data
public class SignUpRequest {

    String email;
    String password;
    String phone_no;
    String first_name;
    String last_name;
    int age;
    char gender;

}
