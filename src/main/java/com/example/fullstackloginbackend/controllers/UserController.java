package com.example.fullstackloginbackend.controllers;

import com.example.fullstackloginbackend.domain.user.UpdateUserDto;
import com.example.fullstackloginbackend.domain.user.User;
import com.example.fullstackloginbackend.domain.user.CreateUserDto;
import com.example.fullstackloginbackend.domain.user.UserResponseDto;
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
  public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid CreateUserDto body) {
    return userService.create(body);
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return userService.getAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
    return userService.getById(id);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDto> updateUser(
      @PathVariable UUID id,
      @ModelAttribute @Valid UpdateUserDto body
  ) {
    return userService.update(id, body);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
    return userService.delete(id);
  }
}
