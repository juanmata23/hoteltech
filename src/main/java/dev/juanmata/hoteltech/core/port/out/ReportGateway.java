package dev.juanmata.hoteltech.core.port.out;

import dev.juanmata.hoteltech.core.entity.Guest;
import dev.juanmata.hoteltech.core.entity.Reservation;
import dev.juanmata.hoteltech.core.entity.Room;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportGateway {
    byte[] generateReservationsReport(
            LocalDate startDate,
            LocalDate endDate,
            List<Reservation> reservations,
            Map<Long, Guest> guestsById,
            Map<Long, Room> roomsById
    );

    byte[] generateCheckoutReceipt(Reservation reservation, Guest guest, Room room);
}