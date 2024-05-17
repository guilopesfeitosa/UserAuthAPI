package com.example.fullstackloginbackend.domain.images;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String url;

  private String publicId;

  public Image(String publicId, String url) {
    this.publicId = publicId;
    this.url = url;
  }
}
