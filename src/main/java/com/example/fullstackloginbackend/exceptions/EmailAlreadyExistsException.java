package com.example.fullstackloginbackend.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
  public EmailAlreadyExistsException() { super("E-mail already exists"); }
  public EmailAlreadyExistsException(String message) {super(message);}
}
