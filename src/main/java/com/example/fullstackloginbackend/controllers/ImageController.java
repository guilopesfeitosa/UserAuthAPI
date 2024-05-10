package com.example.fullstackloginbackend.controllers;

import com.example.fullstackloginbackend.domain.images.Image;
import com.example.fullstackloginbackend.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;

  @PostMapping
  public Image createImage(@RequestParam("file") MultipartFile file) {
    return imageService.uploadImageInDbAndCloudinary(file);
  }
}
