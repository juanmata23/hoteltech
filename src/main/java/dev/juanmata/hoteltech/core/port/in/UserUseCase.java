package dev.juanmata.hoteltech.core.port.in;

import dev.juanmata.hoteltech.core.constant.UserRole;
import dev.juanmata.hoteltech.core.constant.UserStatus;
import dev.juanmata.hoteltech.core.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserUseCase {
    User registerUser(String name, String email, String password, UserRole role);
    void updateUser(Long userId, String name, String email, UserRole role, UserStatus status);
    void deactivateUser(Long userId);
    Optional<User> findById(Long userId);
    Optional<User> findByEmail(String email);
    List<User> listAllUsers();
}