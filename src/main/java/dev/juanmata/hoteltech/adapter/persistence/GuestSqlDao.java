package dev.juanmata.hoteltech.adapter.persistence;

import dev.juanmata.hoteltech.core.entity.Guest;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterConstructorMapper(Guest.class)
public interface GuestSqlDao {

    @SqlUpdate("INSERT INTO guest (name, document_number, phone, email) VALUES (:name, :documentNumber, :phone, :email)")
    @GetGeneratedKeys("guest_id")
    long insert(@Bind("name") String name, @Bind("documentNumber") String documentNumber,
                @Bind("phone") String phone, @Bind("email") String email);

    @SqlUpdate("UPDATE guest SET name = :name, document_number = :documentNumber, phone = :phone, email = :email WHERE guest_id = :guestId")
    void update(@Bind("guestId") Long guestId, @Bind("name") String name, @Bind("documentNumber") String documentNumber,
                @Bind("phone") String phone, @Bind("email") String email);

    @SqlQuery("SELECT guest_id, name, document_number, phone, email FROM guest WHERE guest_id = :guestId")
    Optional<Guest> findById(@Bind("guestId") Long guestId);

    @SqlQuery("SELECT guest_id, name, document_number, phone, email FROM guest WHERE document_number = :documentNumber")
    Optional<Guest> findByDocumentNumber(@Bind("documentNumber") String documentNumber);

    @SqlQuery("SELECT guest_id, name, document_number, phone, email FROM guest")
    List<Guest> findAll();
}