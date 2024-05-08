package com.example.fullstackloginbackend.controllers;

import com.example.fullstackloginbackend.services.ImageService;
import com.example.fullstackloginbackend.utils.CloudinaryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class ImageController {

  @Autowired
  private ImageService imageService;

  @PostMapping
  public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
    return this.imageService.uploadImage(file);
  }
}
