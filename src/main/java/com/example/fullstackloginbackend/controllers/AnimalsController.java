package com.example.fullstackloginbackend.controllers;

import com.example.fullstackloginbackend.domain.animals.Animal;
import com.example.fullstackloginbackend.repositories.AnimalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalsController {
  @Autowired
  private AnimalsRepository repository;

  @GetMapping
  public ResponseEntity<List<Animal>> getAllAnimals() {
    List<Animal> animals = repository.findAll();
    return ResponseEntity.ok(animals);
  }
}
