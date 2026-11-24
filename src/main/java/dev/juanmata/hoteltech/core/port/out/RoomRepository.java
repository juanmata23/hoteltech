package dev.juanmata.hoteltech.core.port.out;

import dev.juanmata.hoteltech.core.constant.RoomStatus;
import dev.juanmata.hoteltech.core.entity.Room;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Room save(Room room);
    void update(Room room);
    void updateStatus(Long roomId, RoomStatus status);
    Optional<Room> findById(Long roomId);
    Optional<Room> findByNumber(String number);
    List<Room> findAll();
}