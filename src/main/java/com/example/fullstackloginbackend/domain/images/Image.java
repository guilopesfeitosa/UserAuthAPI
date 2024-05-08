package com.example.fullstackloginbackend.domain.images;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "images")
@Entity(name = "images")
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  private String url;
}
