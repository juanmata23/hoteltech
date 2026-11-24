package dev.juanmata.hoteltech.adapter.web;

import dev.juanmata.hoteltech.core.constant.RoomStatus;
import dev.juanmata.hoteltech.core.constant.RoomType;
import dev.juanmata.hoteltech.core.port.in.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class HotelWebController {

    private final RoomUseCase roomService;
    private final GuestUseCase guestService;
    private final ReservationUseCase reservationService;
    private final UserUseCase userService;
    private final ReportUseCase reportService;

    public HotelWebController(RoomUseCase roomService, GuestUseCase guestService,
                              ReservationUseCase reservationService, UserUseCase userService,
                              ReportUseCase reportService) {
        this.roomService = roomService;
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.reportService = reportService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // --- HABITACIONES ---
    @GetMapping("/rooms")
    public String listRooms(@RequestParam(required = false) LocalDate checkIn,
                            @RequestParam(required = false) LocalDate checkOut,
                            Model model) {
        if (checkIn != null && checkOut != null) {
            model.addAttribute("rooms", roomService.findAvailableRooms(checkIn, checkOut));
            model.addAttribute("filterMsg", "Disponibles del " + checkIn + " al " + checkOut);
        } else {
            model.addAttribute("rooms", roomService.listAllRooms());
        }
        model.addAttribute("roomTypes", RoomType.values());
        return "rooms";
    }

    @PostMapping("/rooms/new")
    public String createRoom(@RequestParam String number, @RequestParam RoomType type,
                             @RequestParam int capacity, @RequestParam BigDecimal pricePerNight,
                             RedirectAttributes redirect) {
        try {
            roomService.registerRoom(number, type, capacity, pricePerNight);
            redirect.addFlashAttribute("success", "Habitacion creada correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rooms";
    }

    @PostMapping("/rooms/{id}/status")
    public String changeRoomStatus(@PathVariable Long id, @RequestParam RoomStatus status, RedirectAttributes redirect) {
        try {
            roomService.changeOperationalStatus(id, status);
            redirect.addFlashAttribute("success", "Estado actualizado");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rooms";
    }

    // --- HUESPEDES ---
    @GetMapping("/guests")
    public String listGuests(Model model) {
        model.addAttribute("guests", guestService.listAllGuests());
        return "guests";
    }

    @PostMapping("/guests/new")
    public String createGuest(@RequestParam String name, @RequestParam String documentNumber,
                              @RequestParam String phone, @RequestParam String email,
                              RedirectAttributes redirect) {
        try {
            guestService.registerGuest(name, documentNumber, phone, email);
            redirect.addFlashAttribute("success", "Huesped registrado");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/guests";
    }

    // --- RESERVAS Y ESTADIAS ---
    @GetMapping({"/", "/reservations"})
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationService.listAllReservations());
        model.addAttribute("guests", guestService.listAllGuests());
        model.addAttribute("rooms", roomService.listAllRooms());
        return "reservations";
    }

    @PostMapping("/reservations/new")
    public String createReservation(@RequestParam Long guestId, @RequestParam Long roomId,
                                    @RequestParam LocalDate checkInDate, @RequestParam LocalDate checkOutDate,
                                    Authentication auth, RedirectAttributes redirect) {
        try {
            Long userId = userService.findByEmail(auth.getName()).orElseThrow().userId();
            reservationService.createReservation(guestId, roomId, checkInDate, checkOutDate, userId);
            redirect.addFlashAttribute("success", "Reserva creada con exito");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }

    @PostMapping("/reservations/{id}/check-in")
    public String checkIn(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            reservationService.checkIn(id);
            redirect.addFlashAttribute("success", "Check-in registrado");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }

    @PostMapping("/reservations/{id}/check-out")
    public String checkOut(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            reservationService.checkOut(id);
            redirect.addFlashAttribute("success", "Check-out registrado");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }

    @PostMapping("/reservations/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            reservationService.cancelReservation(id);
            redirect.addFlashAttribute("success", "Reserva cancelada");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }

    // --- REPORTES PDF ---
    @GetMapping("/reports/reservations")
    public ResponseEntity<byte[]> downloadReservationsReport(@RequestParam LocalDate startDate,
                                                             @RequestParam LocalDate endDate) {
        byte[] pdf = reportService.generateReservationsPdf(startDate, endDate);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-reservas.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/reports/receipt/{id}")
    public ResponseEntity<byte[]> downloadCheckoutReceipt(@PathVariable Long id) {
        byte[] pdf = reportService.generateCheckoutReceiptPdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}