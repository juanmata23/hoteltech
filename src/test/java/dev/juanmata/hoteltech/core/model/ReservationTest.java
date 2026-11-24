package dev.juanmata.hoteltech.core.model;

import dev.juanmata.hoteltech.core.constant.ReservationStatus;
import dev.juanmata.hoteltech.core.entity.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void shouldCalculateNightsAndTotalPriceCorrectly() {
        Reservation reservation = new Reservation(
                1L,
                LocalDate.of(2026, 11, 25),
                LocalDate.of(2026, 11, 28),
                ReservationStatus.PENDING,
                new BigDecimal("150000"),
                null,
                null,
                1L,
                2L,
                1L
        );

        assertEquals(3L, reservation.getNights());
        assertEquals(new BigDecimal("450000"), reservation.calculateTotalPrice());
    }

    @Test
    void shouldThrowExceptionWhenCheckOutIsNotAfterCheckIn() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(
                1L,
                LocalDate.of(2026, 11, 28),
                LocalDate.of(2026, 11, 25),
                ReservationStatus.PENDING,
                new BigDecimal("150000"),
                null,
                null,
                1L,
                2L,
                1L
        ));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Reservation(
                1L,
                LocalDate.of(2026, 11, 25),
                LocalDate.of(2026, 11, 28),
                ReservationStatus.PENDING,
                BigDecimal.ZERO,
                null,
                null,
                1L,
                2L,
                1L
        ));
    }
}