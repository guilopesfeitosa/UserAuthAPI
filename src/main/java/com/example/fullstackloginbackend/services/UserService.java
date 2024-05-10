package com.example.fullstackloginbackend.services;

import com.example.fullstackloginbackend.domain.images.Image;
import com.example.fullstackloginbackend.domain.user.CreateUserDto;
import com.example.fullstackloginbackend.domain.user.UpdateUserDto;
import com.example.fullstackloginbackend.domain.user.User;
import com.example.fullstackloginbackend.domain.user.UserResponseDto;
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

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ImageService imageService;

  public ResponseEntity<UserResponseDto> create(CreateUserDto body) {
    try {
      Optional<User> optionalUser = userRepository.findByEmail(body.email());

      if (optionalUser.isPresent()) {
        throw new EmailAlreadyExistsException();
      }

      User newUser = new User();
      newUser.setName(body.name());
      newUser.setEmail(body.email());
      newUser.setPassword(passwordEncoder.encode(body.password()));
      userRepository.save(newUser);

      return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDto(
          newUser.getId(),
          newUser.getName(),
          newUser.getEmail(),
          newUser.getProfileImage().getUrl()
      ));
    } catch (Exception e) {
      throw new RuntimeException("Error creating user: ", e);
    }
  }

  public ResponseEntity<List<User>> getAll() {
    try {
      List<User> allUsers = this.userRepository.findAll();
      return ResponseEntity.status(HttpStatus.OK).body(allUsers);
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

  public ResponseEntity<UserResponseDto> update(UUID id, UpdateUserDto body) {
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
