package dev.juanmata.hoteltech.adapter.persistence;

import dev.juanmata.hoteltech.core.constant.RoomStatus;
import dev.juanmata.hoteltech.core.constant.RoomType;
import dev.juanmata.hoteltech.core.entity.Room;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RegisterConstructorMapper(Room.class)
public interface RoomSqlDao {

    @SqlUpdate("INSERT INTO room (number, type, capacity, price_per_night, status) VALUES (:number, :type, :capacity, :pricePerNight, :status)")
    @GetGeneratedKeys("room_id")
    long insert(@Bind("number") String number, @Bind("type") RoomType type, @Bind("capacity") int capacity,
                @Bind("pricePerNight") BigDecimal pricePerNight, @Bind("status") RoomStatus status);

    @SqlUpdate("UPDATE room SET number = :number, type = :type, capacity = :capacity, price_per_night = :pricePerNight WHERE room_id = :roomId")
    void update(@Bind("roomId") Long roomId, @Bind("number") String number, @Bind("type") RoomType type,
                @Bind("capacity") int capacity, @Bind("pricePerNight") BigDecimal pricePerNight);

    @SqlUpdate("UPDATE room SET status = :status WHERE room_id = :roomId")
    void updateStatus(@Bind("roomId") Long roomId, @Bind("status") RoomStatus status);

    @SqlQuery("SELECT room_id, number, type, capacity, price_per_night, status FROM room WHERE room_id = :roomId")
    Optional<Room> findById(@Bind("roomId") Long roomId);

    @SqlQuery("SELECT room_id, number, type, capacity, price_per_night, status FROM room WHERE number = :number")
    Optional<Room> findByNumber(@Bind("number") String number);

    @SqlQuery("SELECT room_id, number, type, capacity, price_per_night, status FROM room")
    List<Room> findAll();
}