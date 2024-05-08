package com.example.fullstackloginbackend.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryHandler {
  Dotenv dotenv = Dotenv.load();

  private final Cloudinary cloudinary;

  public CloudinaryHandler() {
    this.cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    this.cloudinary.config.secure = true;
  }

  public String upload(MultipartFile image) throws IOException {
    Map imageUrl = this.cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("public_id", image.getOriginalFilename()));
    return imageUrl.get("secure_url").toString();
  }
}
