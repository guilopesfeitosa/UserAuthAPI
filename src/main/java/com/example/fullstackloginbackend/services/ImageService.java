package com.example.fullstackloginbackend.services;

import com.example.fullstackloginbackend.domain.images.Image;
import com.example.fullstackloginbackend.repositories.ImageRepository;
import com.example.fullstackloginbackend.utils.CloudinaryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final CloudinaryHandler cloudinaryHandler;
  private final ImageRepository imageRepository;

  public Image uploadImageInDbAndCloudinary(MultipartFile file) {
    try {
      Image image = cloudinaryHandler.upload(file);

      Image newImage = new Image();

      newImage.setUrl(image.getUrl());
      newImage.setPublicId(image.getPublicId());

      imageRepository.save(newImage);

      return newImage;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void removeImageInDbAndCloudinary(UUID id) {
    try {
      Optional<Image> optionalImage = imageRepository.findById(id);

      if (optionalImage.isPresent()) {
        cloudinaryHandler.remove(optionalImage.get().getPublicId());
        imageRepository.delete(optionalImage.get());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
