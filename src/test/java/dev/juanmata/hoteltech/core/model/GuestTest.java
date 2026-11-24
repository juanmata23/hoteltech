package dev.juanmata.hoteltech.core.model;

import dev.juanmata.hoteltech.core.entity.Guest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestTest {

    @Test
    void shouldCreateValidGuest() {
        Guest guest = new Guest(1L, "Carlos Perez", "1049654321", "3101234567", "carlos@email.com");

        assertEquals("Carlos Perez", guest.name());
        assertEquals("1049654321", guest.documentNumber());
    }

    @Test
    void shouldThrowExceptionWhenDocumentOrNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Guest(1L, "", "1049654321", "3101234567", "carlos@email.com"));

        assertThrows(IllegalArgumentException.class, () ->
                new Guest(1L, "Carlos Perez", "   ", "3101234567", "carlos@email.com"));
    }
}