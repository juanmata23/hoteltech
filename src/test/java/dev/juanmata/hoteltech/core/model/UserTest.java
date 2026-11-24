package dev.juanmata.hoteltech.core.model;

import dev.juanmata.hoteltech.core.constant.UserRole;
import dev.juanmata.hoteltech.core.constant.UserStatus;
import dev.juanmata.hoteltech.core.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateValidUserWithRoleAndStatus() {
        User admin = new User(1L, "Admin Hotel", "admin@hoteltech.com", "admin123", UserRole.ADMIN, UserStatus.ACTIVE);

        assertEquals(UserRole.ADMIN, admin.role());
        assertEquals(UserStatus.ACTIVE, admin.status());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
                new User(1L, "Recepcion", "correo-invalido", "recep123", UserRole.RECEPTIONIST, UserStatus.ACTIVE));
    }
}