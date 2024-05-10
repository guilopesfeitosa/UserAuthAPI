package com.example.fullstackloginbackend.services;

import com.example.fullstackloginbackend.domain.images.Image;
import com.example.fullstackloginbackend.domain.user.*;
import com.example.fullstackloginbackend.exceptions.EmailAlreadyExistsException;
import com.example.fullstackloginbackend.exceptions.UserNotFoundException;
import com.example.fullstackloginbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ImageService imageService;

  public ResponseEntity<CreateUserResponseDto> create(CreateUserRequestDto body) {
    try {
      Optional<User> optionalUser = userRepository.findByEmail(body.email());

      if (optionalUser.isPresent()) {
        throw new EmailAlreadyExistsException();
      }

      User newUser = new User(body, passwordEncoder);
      userRepository.save(newUser);

      return ResponseEntity.status(HttpStatus.CREATED).body(new CreateUserResponseDto(
          newUser.getId(),
          newUser.getName(),
          newUser.getEmail()
      ));
    } catch (Exception e) {
      throw new RuntimeException("Error creating user: ", e);
    }
  }

  public ResponseEntity<List<UserResponseDto>> getAll() {
    try {
      List<User> allUsers = this.userRepository.findAll();

      List<UserResponseDto> filteredUsers = allUsers.stream()
          .map(user -> new UserResponseDto(
              user.getId(),
              user.getName(),
              user.getEmail(),
              user.getProfileImage() != null ? user.getProfileImage().getUrl() : null
          ))
          .collect(Collectors.toList());

      return ResponseEntity.status(HttpStatus.OK).body(filteredUsers);
    } catch (Exception e) {
      throw new RuntimeException("Error finding users: ", e);
    }
  }

  public ResponseEntity<UserResponseDto> getById(UUID id) {
    try {
      Optional<User> user = this.userRepository.findById(id);

      if (user.isEmpty()) {
        throw new UserNotFoundException();
      }

      return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(
          user.get().getId(),
          user.get().getName(),
          user.get().getEmail(),
          user.get().getProfileImage().getUrl()
      ));
    } catch (Exception e) {
      throw new RuntimeException("Error finding user: ", e);
    }
  }

  public ResponseEntity<UserResponseDto> update(UUID id, UpdateUserRequestDto body) {
    try {
      Optional<User> optionalUser = this.userRepository.findById(id);

      if (optionalUser .isEmpty()) {
        throw new UserNotFoundException();
      }

      User user = optionalUser.get();

      if (body.name() != null) user.setName(body.name());
      if (body.email() != null) user.setEmail(body.email());
      if (body.password() != null) user.setPassword(passwordEncoder.encode(body.password()));

      if (body.profileImage() != null) {
        if (user.getProfileImage() != null) {
          UUID oldProfileImageId = user.getProfileImage().getId();
          user.setProfileImage(null);
          imageService.removeImageInDbAndCloudinary(oldProfileImageId);
        }

        Image profileImage = imageService.uploadImageInDbAndCloudinary(body.profileImage());
        user.setProfileImage(profileImage);
      }

      userRepository.saveAndFlush(user);

      return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(
          user.getId(),
          user.getName(),
          user.getEmail(),
          user.getProfileImage().getUrl()
      ));
    } catch (Exception e) {
      throw new RuntimeException("Error updating user: ", e);
    }
  }

  public ResponseEntity<String> delete(UUID id) {
    try {
      Optional<User> user = this.userRepository.findById(id);

      if (user.isEmpty()) {
        throw new UserNotFoundException();
      }

      this.userRepository.delete(user.get());
      return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    } catch (Exception e) {
      throw new RuntimeException("Error deleting user: ", e);
    }
  }
}
