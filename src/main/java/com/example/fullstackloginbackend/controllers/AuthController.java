package com.example.fullstackloginbackend.controllers;

import com.example.fullstackloginbackend.domain.user.User;
import com.example.fullstackloginbackend.dto.AuthResponseDto;
import com.example.fullstackloginbackend.dto.SignInRequestDto;
import com.example.fullstackloginbackend.exceptions.InvalidPasswordException;
import com.example.fullstackloginbackend.exceptions.UserNotFoundException;
import com.example.fullstackloginbackend.infra.security.TokenService;
import com.example.fullstackloginbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  @PostMapping("/sign-in")
  public ResponseEntity signin(@RequestBody SignInRequestDto body) {
    User user = this.userRepository.findByEmail(body.email()).orElseThrow(UserNotFoundException::new);

    if (!passwordEncoder.matches(body.password(), user.getPassword())) {
      throw new InvalidPasswordException();
    }

    String token = this.tokenService.generateToken(user);
    return ResponseEntity.ok(new AuthResponseDto(user.getName(), token));
  }
}
