package com.BookMyStay.bookmystay.Exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(Exception exception) {
        ApiError apiError=new ApiError("Username not found"+exception.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError,apiError.getStatusCode());
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(Exception exception) {
        ApiError apiError=new ApiError("Authentication Failed", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError,apiError.getStatusCode());
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(Exception exception) {
        ApiError apiError=new ApiError("Invalid Token", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError,apiError.getStatusCode());
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(Exception exception) {
        ApiError apiError=new ApiError("Access Denied", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(apiError,apiError.getStatusCode());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception exception) {
        ApiError apiError=new ApiError("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiError,apiError.getStatusCode());
    }

}
