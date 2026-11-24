package dev.juanmata.hoteltech.core.service;

import dev.juanmata.hoteltech.core.constant.ReservationStatus;
import dev.juanmata.hoteltech.core.constant.RoomStatus;
import dev.juanmata.hoteltech.core.entity.Reservation;
import dev.juanmata.hoteltech.core.entity.Room;
import dev.juanmata.hoteltech.core.port.in.ReservationUseCase;
import dev.juanmata.hoteltech.core.port.in.RoomUseCase;
import dev.juanmata.hoteltech.core.port.out.GuestRepository;
import dev.juanmata.hoteltech.core.port.out.ReservationRepository;
import dev.juanmata.hoteltech.core.port.out.RoomRepository;
import dev.juanmata.hoteltech.core.port.out.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReservationService implements ReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;
    private final RoomUseCase roomService;

    public ReservationService(
            ReservationRepository reservationRepository,
            RoomRepository roomRepository,
            GuestRepository guestRepository,
            UserRepository userRepository,
            RoomUseCase roomService
    ) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.userRepository = userRepository;
        this.roomService = roomService;
    }

    public Reservation createReservation(
            Long guestId,
            Long roomId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Long registeredByUserId
    ) {
        guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Guest not found"));
        userRepository.findById(registeredByUserId)
                .orElseThrow(() -> new IllegalArgumentException("Registering user not found"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (room.status() == RoomStatus.MAINTENANCE) {
            throw new IllegalStateException("Room is currently under maintenance");
        }
        if (!roomService.isRoomAvailableForDates(roomId, checkInDate, checkOutDate)) {
            throw new IllegalStateException("Room is already reserved for the selected dates");
        }

        // Guarda el precio por noche vigente al momento de reservar
        Reservation reservation = new Reservation(
                null,
                checkInDate,
                checkOutDate,
                ReservationStatus.PENDING,
                room.pricePerNight(),
                null,
                null,
                guestId,
                roomId,
                registeredByUserId
        );

        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.status() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Only PENDING reservations can be cancelled");
        }

        Reservation cancelled = new Reservation(
                reservation.reservationId(),
                reservation.checkInDate(),
                reservation.checkOutDate(),
                ReservationStatus.CANCELLED,
                reservation.pricePerNightAtBooking(),
                reservation.checkedInAt(),
                reservation.checkedOutAt(),
                reservation.guestId(),
                reservation.roomId(),
                reservation.registeredByUserId()
        );
        reservationRepository.update(cancelled);
    }

    public void checkIn(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.status() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Only PENDING reservations can check in");
        }

        Room room = roomRepository.findById(reservation.roomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        if (room.status() != RoomStatus.AVAILABLE) {
            throw new IllegalStateException("Room must be AVAILABLE to check in");
        }

        Reservation checkedIn = new Reservation(
                reservation.reservationId(),
                reservation.checkInDate(),
                reservation.checkOutDate(),
                ReservationStatus.CHECKED_IN,
                reservation.pricePerNightAtBooking(),
                LocalDateTime.now(),
                null,
                reservation.guestId(),
                reservation.roomId(),
                reservation.registeredByUserId()
        );

        reservationRepository.update(checkedIn);
        roomRepository.updateStatus(room.roomId(), RoomStatus.OCCUPIED);
    }

    public void checkOut(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.status() != ReservationStatus.CHECKED_IN) {
            throw new IllegalStateException("Only CHECKED_IN reservations can check out");
        }

        Reservation checkedOut = new Reservation(
                reservation.reservationId(),
                reservation.checkInDate(),
                reservation.checkOutDate(),
                ReservationStatus.CHECKED_OUT,
                reservation.pricePerNightAtBooking(),
                reservation.checkedInAt(),
                LocalDateTime.now(),
                reservation.guestId(),
                reservation.roomId(),
                reservation.registeredByUserId()
        );

        reservationRepository.update(checkedOut);
        roomRepository.updateStatus(reservation.roomId(), RoomStatus.AVAILABLE);
    }

    public Optional<Reservation> findById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public List<Reservation> listAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> listReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }
}