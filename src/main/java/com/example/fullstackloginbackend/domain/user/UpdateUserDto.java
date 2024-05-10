package com.example.fullstackloginbackend.domain.user;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserDto(String name, String email, String password, MultipartFile profileImage) {}
