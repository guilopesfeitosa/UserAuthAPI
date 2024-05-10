package com.example.fullstackloginbackend.domain.user;

import java.util.UUID;

public record UserResponseDto(UUID id, String name, String email, String profileImageUrl) {}
