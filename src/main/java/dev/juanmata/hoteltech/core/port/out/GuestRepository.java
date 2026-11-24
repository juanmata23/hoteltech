package dev.juanmata.hoteltech.core.port.out;

import dev.juanmata.hoteltech.core.entity.Guest;
import java.util.List;
import java.util.Optional;

public interface GuestRepository {
    Guest save(Guest guest);
    void update(Guest guest);
    Optional<Guest> findById(Long guestId);
    Optional<Guest> findByDocumentNumber(String documentNumber);
    List<Guest> findAll();
}