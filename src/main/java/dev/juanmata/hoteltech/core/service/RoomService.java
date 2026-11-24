package dev.juanmata.hoteltech.core.service;

import dev.juanmata.hoteltech.core.constant.RoomStatus;
import dev.juanmata.hoteltech.core.constant.RoomType;
import dev.juanmata.hoteltech.core.entity.Reservation;
import dev.juanmata.hoteltech.core.entity.Room;
import dev.juanmata.hoteltech.core.port.in.RoomUseCase;
import dev.juanmata.hoteltech.core.port.out.ReservationRepository;
import dev.juanmata.hoteltech.core.port.out.RoomRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RoomService implements RoomUseCase {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public Room registerRoom(String number, RoomType type, int capacity, BigDecimal pricePerNight) {
        if (roomRepository.findByNumber(number).isPresent()) {
            throw new IllegalArgumentException("Room number already exists");
        }
        Room newRoom = new Room(null, number, type, capacity, pricePerNight, RoomStatus.AVAILABLE);
        return roomRepository.save(newRoom);
    }

    public void updateRoom(Long roomId, String number, RoomType type, int capacity, BigDecimal pricePerNight) {
        Room existing = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Optional<Room> roomWithNumber = roomRepository.findByNumber(number);
        if (roomWithNumber.isPresent() && !roomWithNumber.get().roomId().equals(roomId)) {
            throw new IllegalArgumentException("Room number is already in use");
        }

        Room updated = new Room(existing.roomId(), number, type, capacity, pricePerNight, existing.status());
        roomRepository.update(updated);
    }

    public void changeOperationalStatus(Long roomId, RoomStatus newStatus) {
        if (newStatus != RoomStatus.AVAILABLE && newStatus != RoomStatus.MAINTENANCE) {
            throw new IllegalArgumentException("Admin can only switch room status between AVAILABLE and MAINTENANCE");
        }
        Room existing = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (existing.status() == RoomStatus.OCCUPIED) {
            throw new IllegalStateException("Cannot change status of an occupied room");
        }
        roomRepository.updateStatus(roomId, newStatus);
    }

    public List<Room> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null || !checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        return roomRepository.findAll().stream()
                .filter(room -> room.status() != RoomStatus.MAINTENANCE)
                .filter(room -> isRoomAvailableForDates(room.roomId(), checkInDate, checkOutDate))
                .toList();
    }

    public boolean isRoomAvailableForDates(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Reservation> activeReservations = reservationRepository.findActiveOrPendingByRoomId(roomId);
        // Dos rangos [A, B) y [C, D) se cruzan si A < D && B > C
        return activeReservations.stream()
                .noneMatch(r -> checkInDate.isBefore(r.checkOutDate()) && checkOutDate.isAfter(r.checkInDate()));
    }

    public Optional<Room> findById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    public List<Room> listAllRooms() {
        return roomRepository.findAll();
    }
}