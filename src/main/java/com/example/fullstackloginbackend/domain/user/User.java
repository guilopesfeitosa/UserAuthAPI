package com.example.fullstackloginbackend.domain.user;

import com.example.fullstackloginbackend.domain.images.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  @Column(unique = true)
  private String email;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @OneToOne
  @JoinColumn(name = "image_id")
  private Image profileImage;

  public User(UUID id, String name, String email, Image profileImage) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.profileImage = profileImage;
  }

  public User(CreateUserRequestDto data, PasswordEncoder passwordEncoder) {
    this.name = data.name();
    this.email = data.email();
    this.password = passwordEncoder.encode(data.password());
  }

  public User(CreateUserRequestDto data) {
    this.name = data.name();
    this.email = data.email();
    this.password = data.password();
  }
}
