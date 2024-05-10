package com.example.fullstackloginbackend.domain.user;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserRequestDto(String name, String email, String password, MultipartFile profileImage) {}
