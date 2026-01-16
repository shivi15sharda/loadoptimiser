package com.teleport.loadoptimiser.exceptionhandling;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
