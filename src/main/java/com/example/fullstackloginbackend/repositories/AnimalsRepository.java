package com.example.fullstackloginbackend.repositories;

import com.example.fullstackloginbackend.domain.animals.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnimalsRepository extends JpaRepository<Animal, UUID> {}
