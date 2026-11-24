package dev.juanmata.hoteltech.adapter.persistence;

import dev.juanmata.hoteltech.core.constant.RoomStatus;
import dev.juanmata.hoteltech.core.entity.Room;
import dev.juanmata.hoteltech.core.port.out.RoomRepository;

import java.util.List;
import java.util.Optional;

public class RoomJdbiAdapter implements RoomRepository {

    private final RoomSqlDao dao;

    public RoomJdbiAdapter(RoomSqlDao dao) {
        this.dao = dao;
    }

    @Override
    public Room save(Room room) {
        long id = dao.insert(room.number(), room.type(), room.capacity(), room.pricePerNight(), room.status());
        return new Room(id, room.number(), room.type(), room.capacity(), room.pricePerNight(), room.status());
    }

    @Override
    public void update(Room room) {
        dao.update(room.roomId(), room.number(), room.type(), room.capacity(), room.pricePerNight());
    }

    @Override
    public void updateStatus(Long roomId, RoomStatus status) {
        dao.updateStatus(roomId, status);
    }

    @Override
    public Optional<Room> findById(Long roomId) {
        return dao.findById(roomId);
    }

    @Override
    public Optional<Room> findByNumber(String number) {
        return dao.findByNumber(number);
    }

    @Override
    public List<Room> findAll() {
        return dao.findAll();
    }
}