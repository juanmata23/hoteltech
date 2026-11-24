package dev.juanmata.hoteltech.core.port.out;

import dev.juanmata.hoteltech.core.constant.ReservationStatus;
import dev.juanmata.hoteltech.core.entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    void update(Reservation reservation);
    Optional<Reservation> findById(Long reservationId);
    List<Reservation> findAll();
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByDateRange(LocalDate startDate, LocalDate endDate);
    List<Reservation> findActiveOrPendingByRoomId(Long roomId);
}