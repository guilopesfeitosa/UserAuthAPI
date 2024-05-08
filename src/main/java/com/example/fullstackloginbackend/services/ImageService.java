package com.example.fullstackloginbackend.services;

import com.example.fullstackloginbackend.utils.CloudinaryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

  @Autowired
  private CloudinaryHandler cloudinaryHandler;

  public String uploadImage(MultipartFile file) throws IOException {
    return this.cloudinaryHandler.upload(file);
  }
}
