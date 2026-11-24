package dev.juanmata.hoteltech.core.port.out;

import dev.juanmata.hoteltech.core.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    void update(User user);
    Optional<User> findById(Long userId);
    Optional<User> findByEmail(String email);
    List<User> findAll();
}