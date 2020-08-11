package com.sunno.accountservice.models.response;

import lombok.Data;
import org.codehaus.jackson.schema.JsonSerializableSchema;

@Data
public class ApiResponse {
    String message;
    Boolean status;

    public ApiResponse(boolean status,String message){
        this.status = status;
        this.message = message;

    }
}
