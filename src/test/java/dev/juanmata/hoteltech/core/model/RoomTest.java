package dev.juanmata.hoteltech.core.model;

import dev.juanmata.hoteltech.core.constant.RoomStatus;
import dev.juanmata.hoteltech.core.constant.RoomType;
import dev.juanmata.hoteltech.core.entity.Room;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void shouldCreateValidRoom() {
        Room room = new Room(1L, "101", RoomType.DOUBLE, 2, new BigDecimal("150000"), RoomStatus.AVAILABLE);

        assertEquals("101", room.number());
        assertEquals(RoomType.DOUBLE, room.type());
        assertEquals(2, room.capacity());
        assertEquals(new BigDecimal("150000"), room.pricePerNight());
        assertEquals(RoomStatus.AVAILABLE, room.status());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegativeOrZero() {
        assertThrows(IllegalArgumentException.class, () ->
                new Room(1L, "101", RoomType.SINGLE, 1, new BigDecimal("-50000"), RoomStatus.AVAILABLE));
    }

    @Test
    void shouldThrowExceptionWhenCapacityIsInvalid() {
        assertThrows(IllegalArgumentException.class, () ->
                new Room(1L, "101", RoomType.SINGLE, 0, new BigDecimal("100000"), RoomStatus.AVAILABLE));
    }
}