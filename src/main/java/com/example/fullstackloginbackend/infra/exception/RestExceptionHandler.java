package com.example.fullstackloginbackend.infra.exception;

import com.example.fullstackloginbackend.exceptions.EmailAlreadyExistsException;
import com.example.fullstackloginbackend.exceptions.InvalidPasswordException;
import com.example.fullstackloginbackend.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  private ResponseEntity<RestErrorMessage> userNotFoundHandler(UserNotFoundException exception) {
    RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  private ResponseEntity<RestErrorMessage> invalidPasswordHandler(InvalidPasswordException exception) {
    RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.UNAUTHORIZED, exception.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(threatResponse);
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  private ResponseEntity<RestErrorMessage> invalidPasswordHandler(EmailAlreadyExistsException exception) {
    RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(threatResponse);
  }
}
