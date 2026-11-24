package dev.juanmata.hoteltech.core.entity;

public record Guest(
        Long guestId,
        String name,
        String documentNumber,
        String phone,
        String email
) {
    public Guest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Guest name cannot be empty");
        }
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new IllegalArgumentException("Guest document number cannot be empty");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Guest phone cannot be empty");
        }
        if (email == null || !email.contains("@") || email.isBlank()) {
            throw new IllegalArgumentException("Valid guest email is required");
        }
    }
}