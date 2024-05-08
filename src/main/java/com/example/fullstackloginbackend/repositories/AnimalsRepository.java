package com.example.fullstackloginbackend.repositories;

import com.example.fullstackloginbackend.domain.animals.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalsRepository extends JpaRepository<Animal, String> {}
