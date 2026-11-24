package dev.juanmata.hoteltech.core.port.in;

import dev.juanmata.hoteltech.core.entity.Guest;

import java.util.List;
import java.util.Optional;

public interface GuestUseCase {
    Guest registerGuest(String name, String documentNumber, String phone, String email);
    void updateGuest(Long guestId, String name, String documentNumber, String phone, String email);
    Optional<Guest> findById(Long guestId);
    List<Guest> listAllGuests();
}