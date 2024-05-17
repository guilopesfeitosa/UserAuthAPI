package com.example.fullstackloginbackend.domain.user;

import java.util.UUID;

public record CreateUserResponseDto(UUID id, String name, String email) {}
