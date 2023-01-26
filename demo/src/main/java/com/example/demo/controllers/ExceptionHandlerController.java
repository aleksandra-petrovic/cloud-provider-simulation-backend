package com.example.demo.controllers;

import com.example.demo.services.ErrorMsgService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

@RestControllerAdvice
public class ExceptionHandlerController {

    private final ErrorMsgService errorMsgService;

    public ExceptionHandlerController(ErrorMsgService errorMsgService){
        this.errorMsgService = errorMsgService;
    }

    //Exceptionhandler {throwable.class}
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleValidationExceptions(MethodArgumentNotValidException exception) throws ParseException {
        Map<String,String> errors = new HashMap<>();
        for(ObjectError error: exception.getBindingResult().getAllErrors()){
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
            this.errorMsgService.log(Long.valueOf(0), fieldName, errorMessage);
        }
        return errors;
    }
}
