package com.BookMyStay.bookmystay.Exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private LocalDateTime timestamp;
    private String error;
    private HttpStatus statusCode;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(String error , HttpStatus statusCode) {
        this();
        this.statusCode = statusCode;
        this.error = error;

    }
}