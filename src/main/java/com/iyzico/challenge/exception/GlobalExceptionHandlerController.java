package com.iyzico.challenge.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity handleDataNotFoundException(DataNotFoundException e) {
        logger.error("Data not found! Details: ", e);
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        logger.error("An error occurred! Details: ", e);
        return new ResponseEntity(messageSource.getMessage("exception.general", null, Locale.getDefault()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("An error occurred! Details: ", e);
        return new ResponseEntity(messageSource.getMessage("exception.badrequest", null, Locale.getDefault()), HttpStatus.BAD_REQUEST);
    }
}
