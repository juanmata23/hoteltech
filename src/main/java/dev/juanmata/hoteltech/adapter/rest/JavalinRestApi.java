package dev.juanmata.hoteltech.adapter.rest;

import dev.juanmata.hoteltech.core.constant.RoomStatus;
import dev.juanmata.hoteltech.core.constant.RoomType;
import dev.juanmata.hoteltech.core.port.in.GuestUseCase;
import dev.juanmata.hoteltech.core.port.in.ReservationUseCase;
import dev.juanmata.hoteltech.core.port.in.RoomUseCase;
import io.javalin.Javalin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class JavalinRestApi {

    private final RoomUseCase roomService;
    private final GuestUseCase guestService;
    private final ReservationUseCase reservationService;

    public JavalinRestApi(RoomUseCase roomService, GuestUseCase guestService, ReservationUseCase reservationService) {
        this.roomService = roomService;
        this.guestService = guestService;
        this.reservationService = reservationService;
    }

    public void start(int port) {
        Javalin app = Javalin.create().start(port);

        // Manejo global de errores de negocio en JSON
        app.exception(IllegalArgumentException.class, (e, ctx) ->
                ctx.status(400).json(Map.of("error", e.getMessage())));
        app.exception(IllegalStateException.class, (e, ctx) ->
                ctx.status(409).json(Map.of("error", e.getMessage())));

        // --- HABITACIONES ---
        app.get("/api/rooms", ctx -> ctx.json(roomService.listAllRooms()));

        app.get("/api/rooms/available", ctx -> {
            LocalDate checkIn = LocalDate.parse(ctx.queryParam("checkIn"));
            LocalDate checkOut = LocalDate.parse(ctx.queryParam("checkOut"));
            ctx.json(roomService.findAvailableRooms(checkIn, checkOut));
        });

        app.post("/api/rooms", ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            var created = roomService.registerRoom(
                    body.get("number").toString(),
                    RoomType.valueOf(body.get("type").toString()),
                    Integer.parseInt(body.get("capacity").toString()),
                    new BigDecimal(body.get("pricePerNight").toString())
            );
            ctx.status(201).json(created);
        });

        app.patch("/api/rooms/{id}/status", ctx -> {
            Long id = Long.parseLong(ctx.pathParam("id"));
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            roomService.changeOperationalStatus(id, RoomStatus.valueOf(body.get("status").toString()));
            ctx.status(204);
        });

        // --- HUESPEDES ---
        app.get("/api/guests", ctx -> ctx.json(guestService.listAllGuests()));

        app.post("/api/guests", ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            var created = guestService.registerGuest(
                    body.get("name").toString(),
                    body.get("documentNumber").toString(),
                    body.get("phone").toString(),
                    body.get("email").toString()
            );
            ctx.status(201).json(created);
        });

        // --- RESERVAS Y ESTADIAS ---
        app.get("/api/reservations", ctx -> ctx.json(reservationService.listAllReservations()));

        app.post("/api/reservations", ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            var created = reservationService.createReservation(
                    Long.parseLong(body.get("guestId").toString()),
                    Long.parseLong(body.get("roomId").toString()),
                    LocalDate.parse(body.get("checkInDate").toString()),
                    LocalDate.parse(body.get("checkOutDate").toString()),
                    Long.parseLong(body.get("registeredByUserId").toString())
            );
            ctx.status(201).json(created);
        });

        app.post("/api/reservations/{id}/check-in", ctx -> {
            reservationService.checkIn(Long.parseLong(ctx.pathParam("id")));
            ctx.status(204);
        });

        app.post("/api/reservations/{id}/check-out", ctx -> {
            reservationService.checkOut(Long.parseLong(ctx.pathParam("id")));
            ctx.status(204);
        });

        app.post("/api/reservations/{id}/cancel", ctx -> {
            reservationService.cancelReservation(Long.parseLong(ctx.pathParam("id")));
            ctx.status(204);
        });
    }
}