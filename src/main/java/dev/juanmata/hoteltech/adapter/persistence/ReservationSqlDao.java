package dev.juanmata.hoteltech.adapter.persistence;

import dev.juanmata.hoteltech.core.constant.ReservationStatus;
import dev.juanmata.hoteltech.core.entity.Reservation;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RegisterConstructorMapper(ReservationSqlDao.ReservationRow.class)
public interface ReservationSqlDao {

    // Record auxiliar que refleja exactamente las columnas de la tabla en SQLite
    record ReservationRow(
            Long reservationId,
            String checkInDate,
            String checkOutDate,
            String status,
            BigDecimal pricePerNightAtBooking,
            String checkedInAt,
            String checkedOutAt,
            Long guestId,
            Long roomId,
            Long registeredByUserId
    ) {
        public Reservation toDomain() {
            return new Reservation(
                    reservationId,
                    LocalDate.parse(checkInDate),
                    LocalDate.parse(checkOutDate),
                    ReservationStatus.valueOf(status),
                    pricePerNightAtBooking,
                    checkedInAt != null ? LocalDateTime.parse(checkedInAt) : null,
                    checkedOutAt != null ? LocalDateTime.parse(checkedOutAt) : null,
                    guestId,
                    roomId,
                    registeredByUserId
            );
        }
    }

    @SqlUpdate("""
            INSERT INTO reservation (check_in_date, check_out_date, status, price_per_night_at_booking, checked_in_at, checked_out_at, guest_id, room_id, registered_by_user_id)
            VALUES (:checkInDate, :checkOutDate, :status, :pricePerNightAtBooking, :checkedInAt, :checkedOutAt, :guestId, :roomId, :registeredByUserId)
            """)
    @GetGeneratedKeys("reservation_id")
    long insert(@Bind("checkInDate") String checkInDate, @Bind("checkOutDate") String checkOutDate,
                @Bind("status") String status, @Bind("pricePerNightAtBooking") BigDecimal pricePerNightAtBooking,
                @Bind("checkedInAt") String checkedInAt, @Bind("checkedOutAt") String checkedOutAt,
                @Bind("guestId") Long guestId, @Bind("roomId") Long roomId, @Bind("registeredByUserId") Long registeredByUserId);

    @SqlUpdate("""
            UPDATE reservation
            SET check_in_date = :checkInDate, check_out_date = :checkOutDate, status = :status,
                price_per_night_at_booking = :pricePerNightAtBooking, checked_in_at = :checkedInAt, checked_out_at = :checkedOutAt
            WHERE reservation_id = :reservationId
            """)
    void update(@Bind("reservationId") Long reservationId, @Bind("checkInDate") String checkInDate,
                @Bind("checkOutDate") String checkOutDate, @Bind("status") String status,
                @Bind("pricePerNightAtBooking") BigDecimal pricePerNightAtBooking,
                @Bind("checkedInAt") String checkedInAt, @Bind("checkedOutAt") String checkedOutAt);

    @SqlQuery("SELECT * FROM reservation WHERE reservation_id = :reservationId")
    Optional<ReservationRow> findById(@Bind("reservationId") Long reservationId);

    @SqlQuery("SELECT * FROM reservation ORDER BY reservation_id DESC")
    List<ReservationRow> findAll();

    @SqlQuery("SELECT * FROM reservation WHERE status = :status")
    List<ReservationRow> findByStatus(@Bind("status") String status);

    @SqlQuery("SELECT * FROM reservation WHERE check_in_date >= :startDate AND check_out_date <= :endDate")
    List<ReservationRow> findByDateRange(@Bind("startDate") String startDate, @Bind("endDate") String endDate);

    @SqlQuery("SELECT * FROM reservation WHERE room_id = :roomId AND status IN ('PENDING', 'CHECKED_IN')")
    List<ReservationRow> findActiveOrPendingByRoomId(@Bind("roomId") Long roomId);
}