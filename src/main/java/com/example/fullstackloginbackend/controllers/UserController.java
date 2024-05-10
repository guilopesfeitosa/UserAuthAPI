package com.example.fullstackloginbackend.controllers;

import com.example.fullstackloginbackend.domain.user.*;
import com.example.fullstackloginbackend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping
  public ResponseEntity<CreateUserResponseDto> createUser(@RequestBody @Valid CreateUserRequestDto body) {
    return userService.create(body);
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    return userService.getAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
    return userService.getById(id);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDto> updateUser(
      @PathVariable UUID id,
      @ModelAttribute @Valid UpdateUserRequestDto body
  ) {
    return userService.update(id, body);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
    return userService.delete(id);
  }
}
