package com.example.flightbookingsystemwithspringboot.exception;

import com.example.flightbookingsystemwithspringboot.constants.DetailedErrors;
import com.example.flightbookingsystemwithspringboot.resource.FailureResource;
import com.example.flightbookingsystemwithspringboot.resource.FailureResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        FailureResponse failureResponse = new FailureResponse(ex.getBindingResult().getFieldErrors().stream().findFirst().get().getField(), DetailedErrors.INVALID_INPUT.getReasonCode(), DetailedErrors.INVALID_INPUT.getReasonText());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FailureResource(failureResponse));
    }
}
