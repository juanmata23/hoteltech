package dev.juanmata.hoteltech.core.entity;

import dev.juanmata.hoteltech.core.constant.RoomStatus;
import dev.juanmata.hoteltech.core.constant.RoomType;

import java.math.BigDecimal;

public record Room(
        Long roomId,
        String number,
        RoomType type,
        int capacity,
        BigDecimal pricePerNight,
        RoomStatus status
) {
    public Room {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Room number cannot be empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Room type is required");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Room capacity must be greater than zero");
        }
        if (pricePerNight == null || pricePerNight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price per night must be greater than zero");
        }
        if (status == null) {
            throw new IllegalArgumentException("Room status is required");
        }
    }
}