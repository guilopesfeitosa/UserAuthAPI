package com.example.fullstackloginbackend.services;

import com.example.fullstackloginbackend.domain.images.Image;
import com.example.fullstackloginbackend.domain.user.*;
import com.example.fullstackloginbackend.exceptions.EmailAlreadyExistsException;
import com.example.fullstackloginbackend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private ImageService imageService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;


  @Test
  public void shouldCreateUser() {
    CreateUserRequestDto requestDto = new CreateUserRequestDto("John Doe", "john@doe.com", "password");
    User newUser = new User(requestDto, passwordEncoder);

    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(newUser);

    ResponseEntity<CreateUserResponseDto> response = userService.create(requestDto);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(newUser.getId(), Objects.requireNonNull(response.getBody()).id());
    assertEquals(newUser.getName(), response.getBody().name());
    assertEquals(newUser.getEmail(), response.getBody().email());
  }

  @Test
  void shouldThrowEmailAlreadyExistsExceptionInCreateUser() {
    CreateUserRequestDto requestDto = new CreateUserRequestDto("John Doe", "john@doe.com", "password");
    User newUser = new User(requestDto, passwordEncoder);

    when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenReturn(newUser);

    userService.create(requestDto);

    when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(newUser));
    doThrow(new EmailAlreadyExistsException()).when(userRepository).save(any(User.class));

    assertThrows(EmailAlreadyExistsException.class, () -> userService.create(requestDto));
  }

  @Test
  public void shouldReturnAllUsers() {
    User user1 = new User(UUID.randomUUID(), "John Doe", "john@example.com", "password456", null);
    User user2 = new User(UUID.randomUUID(), "Jane Doe", "jane@example.com", "password123", null);

    List<User> users = new ArrayList<>();
    users.add(user1);
    users.add(user2);

    when(userRepository.findAll()).thenReturn(users);

    ResponseEntity<List<UserResponseDto>> response = userService.getAll();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(users.size(), Objects.requireNonNull(response.getBody()).size());

    List<UserResponseDto> expectedUsers = Arrays.asList(
        new UserResponseDto(
            user1.getId(),
            user1.getName(),
            user1.getEmail(),
            user1.getProfileImage() != null ? user1.getProfileImage().getUrl() : null
        ),
        new UserResponseDto(
            user2.getId(),
            user2.getName(),
            user2.getEmail(),
            user2.getProfileImage() != null ? user2.getProfileImage().getUrl() : null
        )
    );

    assertEquals(expectedUsers, response.getBody());
  }

  @Test
  void shouldReturnUserById() {
    UUID id = UUID.randomUUID();
    User user = new User(id, "John Doe", "john@example.com", "password123", null);

    when(userRepository.findById(id)).thenReturn(Optional.of(user));

    ResponseEntity<UserResponseDto> response = userService.getById(id);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    UserResponseDto expectedUser = new UserResponseDto(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getProfileImage() != null ? user.getProfileImage().getUrl() : null);

    assertEquals(expectedUser, response.getBody());
  }

  @Test
  void shouldUpdateUser_noProfileImage() {
    UUID id = UUID.randomUUID();

    UpdateUserRequestDto newUserData = new UpdateUserRequestDto(
        "New Name",
        "new@email.com",
        "newPassword123",
        null
    );
    User originalUserData = new User(id, "John Doe", "john@example.com", "password", null);

    when(userRepository.findById(id)).thenReturn(Optional.of(originalUserData));
    ResponseEntity<UserResponseDto> response = userService.update(id, newUserData);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    UserResponseDto updatedUser = response.getBody();

    assert updatedUser != null;
    assertEquals(newUserData.name(), updatedUser.name());
    assertEquals(newUserData.email(), updatedUser.email());
    assertNull(updatedUser.profileImageUrl());

    verify(userRepository).saveAndFlush(originalUserData);
  }

  @Test
  void shouldUpdateUser_withProfileImage() {
    String filename = "example.jpg";
    String content = "conteúdo da imagem";
    byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
    MultipartFile newProfileImage = new MockMultipartFile(filename, filename, "image/jpeg", contentBytes);

    UUID id = UUID.randomUUID();

    UpdateUserRequestDto user = new UpdateUserRequestDto(
        "John Doe",
        "john@example.com",
        "password123",
        newProfileImage
    );

    User existingUser = new User(id, "Original Name", "original@example.com", "originalPassword", null);

    when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

    Image newImage = new Image("newPublicId", "newImageUrl");
    when(imageService.uploadImageInDbAndCloudinary(newProfileImage)).thenReturn(newImage);

    ResponseEntity<UserResponseDto> response = userService.update(id, user);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    UserResponseDto updatedUser = response.getBody();
    assert updatedUser != null;
    assertEquals("John Doe", updatedUser.name());
    assertEquals("john@example.com", updatedUser.email());

    assertNotNull(updatedUser.profileImageUrl());
    assertEquals("newImageUrl", updatedUser.profileImageUrl());

    verify(userRepository).saveAndFlush(existingUser);
  }

  @Test
  void shouldDeleteUser() {
    UUID id = UUID.randomUUID();

    User existingUser = new User(id, "John Doe", "john@example.com", "password123", null);

    when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

    ResponseEntity<String> response = userService.delete(id);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("User deleted successfully", response.getBody());

    verify(userRepository).delete(existingUser);
  }
}