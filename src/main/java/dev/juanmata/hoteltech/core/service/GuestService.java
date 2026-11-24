package dev.juanmata.hoteltech.core.service;

import dev.juanmata.hoteltech.core.entity.Guest;
import dev.juanmata.hoteltech.core.port.in.GuestUseCase;
import dev.juanmata.hoteltech.core.port.out.GuestRepository;

import java.util.List;
import java.util.Optional;

public class GuestService implements GuestUseCase {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Guest registerGuest(String name, String documentNumber, String phone, String email) {
        if (guestRepository.findByDocumentNumber(documentNumber).isPresent()) {
            throw new IllegalArgumentException("Guest with this document number already exists");
        }
        Guest newGuest = new Guest(null, name, documentNumber, phone, email);
        return guestRepository.save(newGuest);
    }

    public void updateGuest(Long guestId, String name, String documentNumber, String phone, String email) {
        Guest existing = guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Guest not found"));

        Optional<Guest> guestWithDoc = guestRepository.findByDocumentNumber(documentNumber);
        if (guestWithDoc.isPresent() && !guestWithDoc.get().guestId().equals(guestId)) {
            throw new IllegalArgumentException("Document number is already assigned to another guest");
        }

        Guest updated = new Guest(existing.guestId(), name, documentNumber, phone, email);
        guestRepository.update(updated);
    }

    public Optional<Guest> findById(Long guestId) {
        return guestRepository.findById(guestId);
    }

    public List<Guest> listAllGuests() {
        return guestRepository.findAll();
    }
}