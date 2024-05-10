package com.example.fullstackloginbackend.domain.animals;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "animals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Animal {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String name;

  private String description;
}
