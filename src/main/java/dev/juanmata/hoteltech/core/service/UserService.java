package dev.juanmata.hoteltech.core.service;

import dev.juanmata.hoteltech.core.constant.UserRole;
import dev.juanmata.hoteltech.core.constant.UserStatus;
import dev.juanmata.hoteltech.core.entity.User;
import dev.juanmata.hoteltech.core.port.in.UserUseCase;
import dev.juanmata.hoteltech.core.port.out.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService implements UserUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String email, String password, UserRole role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }
        User newUser = new User(null, name, email, password, role, UserStatus.ACTIVE);
        return userRepository.save(newUser);
    }

    public void updateUser(Long userId, String name, String email, UserRole role, UserStatus status) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<User> userWithEmail = userRepository.findByEmail(email);
        if (userWithEmail.isPresent() && !userWithEmail.get().userId().equals(userId)) {
            throw new IllegalArgumentException("Email is already in use by another user");
        }

        User updated = new User(existing.userId(), name, email, existing.password(), role, status);
        userRepository.update(updated);
    }

    public void deactivateUser(Long userId) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User deactivated = new User(
                existing.userId(),
                existing.name(),
                existing.email(),
                existing.password(),
                existing.role(),
                UserStatus.INACTIVE
        );
        userRepository.update(deactivated);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }
}