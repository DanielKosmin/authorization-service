package com.kosmin.authorization.exception;

import com.kosmin.authorization.model.Response;
import com.kosmin.authorization.model.Status;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JakartaValidatorException {
  // Exception thrown when Jakarta annotation throws exception
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<String> validationErrors = new ArrayList<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach((error) -> validationErrors.add(error.getDefaultMessage()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Response.builder().validationErrors(validationErrors).status(Status.FAILURE).build());
  }

  // Exception thrown when request body is empty
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Response> handleInvalidJson(HttpMessageNotReadableException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Response.builder().errorMessage(ex.getMessage()).status(Status.FAILURE).build());
  }
}
