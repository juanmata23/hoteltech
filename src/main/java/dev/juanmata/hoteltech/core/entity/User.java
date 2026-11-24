package dev.juanmata.hoteltech.core.entity;

import dev.juanmata.hoteltech.core.constant.UserRole;
import dev.juanmata.hoteltech.core.constant.UserStatus;

public record User(
        Long userId,
        String name,
        String email,
        String password,
        UserRole role,
        UserStatus status
) {
    public User {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        if (email == null || !email.contains("@") || email.isBlank()) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must have at least 4 characters");
        }
        if (role == null) {
            throw new IllegalArgumentException("User role is required");
        }
        if (status == null) {
            throw new IllegalArgumentException("User status is required");
        }
    }
}