package dev.juanmata.hoteltech.core.entity;

import dev.juanmata.hoteltech.core.constant.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record Reservation(
        Long reservationId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        ReservationStatus status,
        BigDecimal pricePerNightAtBooking,
        LocalDateTime checkedInAt,
        LocalDateTime checkedOutAt,
        Long guestId,
        Long roomId,
        Long registeredByUserId
) {
    public Reservation {
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required");
        }
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        if (status == null) {
            throw new IllegalArgumentException("Reservation status is required");
        }
        if (pricePerNightAtBooking == null || pricePerNightAtBooking.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Agreed price per night must be greater than zero");
        }
        if (guestId == null || roomId == null || registeredByUserId == null) {
            throw new IllegalArgumentException("Guest, room, and user IDs are required");
        }
    }

    public long getNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public BigDecimal calculateTotalPrice() {
        return pricePerNightAtBooking.multiply(BigDecimal.valueOf(getNights()));
    }
}