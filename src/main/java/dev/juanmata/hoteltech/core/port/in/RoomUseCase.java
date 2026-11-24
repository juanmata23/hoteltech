package dev.juanmata.hoteltech.core.port.in;

import dev.juanmata.hoteltech.core.constant.RoomStatus;
import dev.juanmata.hoteltech.core.constant.RoomType;
import dev.juanmata.hoteltech.core.entity.Room;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomUseCase {
    Room registerRoom(String number, RoomType type, int capacity, BigDecimal pricePerNight);
    void updateRoom(Long roomId, String number, RoomType type, int capacity, BigDecimal pricePerNight);
    void changeOperationalStatus(Long roomId, RoomStatus newStatus);
    List<Room> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate);
    boolean isRoomAvailableForDates(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
    Optional<Room> findById(Long roomId);
    List<Room> listAllRooms();
}