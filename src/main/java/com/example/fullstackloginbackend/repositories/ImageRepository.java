package com.example.fullstackloginbackend.repositories;

import com.example.fullstackloginbackend.domain.images.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {}
