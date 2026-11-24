package dev.juanmata.hoteltech.adapter.persistence;

import dev.juanmata.hoteltech.core.entity.Guest;
import dev.juanmata.hoteltech.core.port.out.GuestRepository;

import java.util.List;
import java.util.Optional;

public class GuestJdbiAdapter implements GuestRepository {

    private final GuestSqlDao dao;

    public GuestJdbiAdapter(GuestSqlDao dao) {
        this.dao = dao;
    }

    @Override
    public Guest save(Guest guest) {
        long id = dao.insert(guest.name(), guest.documentNumber(), guest.phone(), guest.email());
        return new Guest(id, guest.name(), guest.documentNumber(), guest.phone(), guest.email());
    }

    @Override
    public void update(Guest guest) {
        dao.update(guest.guestId(), guest.name(), guest.documentNumber(), guest.phone(), guest.email());
    }

    @Override
    public Optional<Guest> findById(Long guestId) {
        return dao.findById(guestId);
    }

    @Override
    public Optional<Guest> findByDocumentNumber(String documentNumber) {
        return dao.findByDocumentNumber(documentNumber);
    }

    @Override
    public List<Guest> findAll() {
        return dao.findAll();
    }
}