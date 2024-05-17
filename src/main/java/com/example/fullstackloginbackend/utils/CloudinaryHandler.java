package com.example.fullstackloginbackend.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.fullstackloginbackend.domain.images.Image;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class CloudinaryHandler {
  Dotenv dotenv = Dotenv.load();
  private final Cloudinary cloudinary;

  public CloudinaryHandler() {
    this.cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    this.cloudinary.config.secure = true;
  }

  public Image upload(MultipartFile image) throws IOException {
    Image imageData = new Image();
    UUID uuid = UUID.randomUUID();
    String publicId = "image_" + uuid;

    var imageCloudinary = this.cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
        "public_id", publicId
    ));

    imageData.setUrl(imageCloudinary.get("secure_url").toString());
    imageData.setPublicId(imageCloudinary.get("public_id").toString());

    return imageData;
  }

  public void remove(String publicId) throws IOException {
    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
  }
}
