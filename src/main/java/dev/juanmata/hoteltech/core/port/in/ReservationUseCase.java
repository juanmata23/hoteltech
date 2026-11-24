package dev.juanmata.hoteltech.core.port.in;

import dev.juanmata.hoteltech.core.constant.ReservationStatus;
import dev.juanmata.hoteltech.core.entity.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationUseCase {
    Reservation createReservation(Long guestId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate, Long registeredByUserId);
    void cancelReservation(Long reservationId);
    void checkIn(Long reservationId);
    void checkOut(Long reservationId);
    Optional<Reservation> findById(Long reservationId);
    List<Reservation> listAllReservations();
    List<Reservation> listReservationsByStatus(ReservationStatus status);
}