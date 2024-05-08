package com.example.fullstackloginbackend.domain.animals;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "animals")
@Entity(name = "animals")
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Animal {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  private String name;

  private String description;
}
