package com.store.store.exceptionHandle;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String,String>> handleValidationException(ConstraintViolationException ex){
        Map<String,String> error=new HashMap<>();
        ex.getConstraintViolations().forEach(violation->{
            String name=violation.getPropertyPath().toString();
            String message=violation.getMessage();
            error.put(name,message);
        });
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}

