package dev.juanmata.hoteltech.adapter.persistence;

import dev.juanmata.hoteltech.adapter.persistence.ReservationSqlDao.ReservationRow;
import dev.juanmata.hoteltech.core.constant.ReservationStatus;
import dev.juanmata.hoteltech.core.entity.Reservation;
import dev.juanmata.hoteltech.core.port.out.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReservationJdbiAdapter implements ReservationRepository {

    private final ReservationSqlDao dao;

    public ReservationJdbiAdapter(ReservationSqlDao dao) {
        this.dao = dao;
    }

    @Override
    public Reservation save(Reservation r) {
        long id = dao.insert(
                r.checkInDate().toString(),
                r.checkOutDate().toString(),
                r.status().name(),
                r.pricePerNightAtBooking(),
                r.checkedInAt() != null ? r.checkedInAt().toString() : null,
                r.checkedOutAt() != null ? r.checkedOutAt().toString() : null,
                r.guestId(),
                r.roomId(),
                r.registeredByUserId()
        );
        return new Reservation(id, r.checkInDate(), r.checkOutDate(), r.status(), r.pricePerNightAtBooking(),
                r.checkedInAt(), r.checkedOutAt(), r.guestId(), r.roomId(), r.registeredByUserId());
    }

    @Override
    public void update(Reservation r) {
        dao.update(
                r.reservationId(),
                r.checkInDate().toString(),
                r.checkOutDate().toString(),
                r.status().name(),
                r.pricePerNightAtBooking(),
                r.checkedInAt() != null ? r.checkedInAt().toString() : null,
                r.checkedOutAt() != null ? r.checkedOutAt().toString() : null
        );
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        return dao.findById(reservationId).map(ReservationRow::toDomain);
    }

    @Override
    public List<Reservation> findAll() {
        return dao.findAll().stream().map(ReservationRow::toDomain).toList();
    }

    @Override
    public List<Reservation> findByStatus(ReservationStatus status) {
        return dao.findByStatus(status.name()).stream().map(ReservationRow::toDomain).toList();
    }

    @Override
    public List<Reservation> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return dao.findByDateRange(startDate.toString(), endDate.toString()).stream().map(ReservationRow::toDomain).toList();
    }

    @Override
    public List<Reservation> findActiveOrPendingByRoomId(Long roomId) {
        return dao.findActiveOrPendingByRoomId(roomId).stream().map(ReservationRow::toDomain).toList();
    }
}