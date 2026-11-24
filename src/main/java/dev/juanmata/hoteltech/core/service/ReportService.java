package dev.juanmata.hoteltech.core.service;

import dev.juanmata.hoteltech.core.constant.ReservationStatus;
import dev.juanmata.hoteltech.core.entity.Guest;
import dev.juanmata.hoteltech.core.entity.Reservation;
import dev.juanmata.hoteltech.core.entity.Room;
import dev.juanmata.hoteltech.core.port.in.ReportUseCase;
import dev.juanmata.hoteltech.core.port.out.GuestRepository;
import dev.juanmata.hoteltech.core.port.out.ReportGateway;
import dev.juanmata.hoteltech.core.port.out.ReservationRepository;
import dev.juanmata.hoteltech.core.port.out.RoomRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReportService implements ReportUseCase {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final ReportGateway reportGateway;

    public ReportService(
            ReservationRepository reservationRepository,
            GuestRepository guestRepository,
            RoomRepository roomRepository,
            ReportGateway reportGateway
    ) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.reportGateway = reportGateway;
    }

    public byte[] generateReservationsPdf(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be on or after start date");
        }
        List<Reservation> reservations = reservationRepository.findByDateRange(startDate, endDate);
        Map<Long, Guest> guestsById = guestRepository.findAll().stream()
                .collect(Collectors.toMap(Guest::guestId, Function.identity()));
        Map<Long, Room> roomsById = roomRepository.findAll().stream()
                .collect(Collectors.toMap(Room::roomId, Function.identity()));

        return reportGateway.generateReservationsReport(startDate, endDate, reservations, guestsById, roomsById);
    }

    public byte[] generateCheckoutReceiptPdf(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.status() != ReservationStatus.CHECKED_OUT) {
            throw new IllegalStateException("Checkout receipt is only available for CHECKED_OUT reservations");
        }

        Guest guest = guestRepository.findById(reservation.guestId())
                .orElseThrow(() -> new IllegalArgumentException("Guest not found"));
        Room room = roomRepository.findById(reservation.roomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        return reportGateway.generateCheckoutReceipt(reservation, guest, room);
    }
}