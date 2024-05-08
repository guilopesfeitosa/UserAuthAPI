package com.example.fullstackloginbackend.controllers;

import com.example.fullstackloginbackend.domain.user.UpdateUserDto;
import com.example.fullstackloginbackend.domain.user.User;
import com.example.fullstackloginbackend.domain.user.CreateUserDto;
import com.example.fullstackloginbackend.domain.user.UserResponseDto;
import com.example.fullstackloginbackend.exceptions.EmailAlreadyExistsException;
import com.example.fullstackloginbackend.exceptions.UserNotFoundException;
import com.example.fullstackloginbackend.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @PostMapping
  public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid CreateUserDto body) {
    Optional<User> user = this.userRepository.findByEmail(body.email());

    if (user.isPresent()) {
      throw new EmailAlreadyExistsException();
    }

    User newUser = new User();
    newUser.setName(body.name());
    newUser.setEmail(body.email());
    newUser.setPassword(passwordEncoder.encode(body.password()));
    this.userRepository.save(newUser);

    return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDto(
        newUser.getId(),
        newUser.getName(),
        newUser.getEmail()
    ));
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> allUsers = this.userRepository.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(allUsers);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id) {
    Optional<User> user = this.userRepository.findById(id);

    if (user.isEmpty()) {
      throw new UserNotFoundException();
    }

    return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(
        user.get().getId(),
        user.get().getName(),
        user.get().getEmail()
    ));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDto> updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserDto body) {
    Optional<User> optionalUser = this.userRepository.findById(id);

    if (optionalUser .isEmpty()) {
      throw new UserNotFoundException();
    }

    User user = optionalUser.get();

    if (body.name() != null) user.setName(body.name());
    if (body.email() != null) user.setEmail(body.email());
    if (body.password() != null) user.setPassword(passwordEncoder.encode(body.password()));

    this.userRepository.saveAndFlush(user);

    return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(
        user.getId(),
        user.getName(),
        user.getEmail()
    ));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable String id) {
    Optional<User> user = this.userRepository.findById(id);

    if (user.isEmpty()) {
      throw new UserNotFoundException();
    }

    this.userRepository.delete(user.get());
    return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
  }
}
