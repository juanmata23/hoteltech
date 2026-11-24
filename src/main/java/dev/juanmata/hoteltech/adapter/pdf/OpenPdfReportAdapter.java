package dev.juanmata.hoteltech.adapter.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import dev.juanmata.hoteltech.core.entity.Guest;
import dev.juanmata.hoteltech.core.entity.Reservation;
import dev.juanmata.hoteltech.core.entity.Room;
import dev.juanmata.hoteltech.core.port.out.ReportGateway;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class OpenPdfReportAdapter implements ReportGateway {

    @Override
    public byte[] generateReservationsReport(
            LocalDate startDate,
            LocalDate endDate,
            List<Reservation> reservations,
            Map<Long, Guest> guestsById,
            Map<Long, Room> roomsById
    ) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        document.add(new Paragraph("HotelTech - Reporte de Reservas", titleFont));
        document.add(new Paragraph("Rango: " + startDate + " a " + endDate, bodyFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        String[] headers = {"ID", "Huesped", "Habitacion", "Entrada - Salida", "Estado", "Total ($)"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new Color(44, 62, 80));
            cell.setPadding(6);
            table.addCell(cell);
        }

        BigDecimal grandTotal = BigDecimal.ZERO;
        for (Reservation r : reservations) {
            Guest guest = guestsById.get(r.guestId());
            Room room = roomsById.get(r.roomId());
            BigDecimal total = r.calculateTotalPrice();
            grandTotal = grandTotal.add(total);

            table.addCell(new Phrase(String.valueOf(r.reservationId()), bodyFont));
            table.addCell(new Phrase(guest != null ? guest.name() : "N/A", bodyFont));
            table.addCell(new Phrase(room != null ? room.number() : "N/A", bodyFont));
            table.addCell(new Phrase(r.checkInDate() + " / " + r.checkOutDate(), bodyFont));
            table.addCell(new Phrase(r.status().name(), bodyFont));
            table.addCell(new Phrase(total.toPlainString(), bodyFont));
        }

        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total acumulado del periodo: $" + grandTotal.toPlainString(), titleFont));

        document.close();
        return out.toByteArray();
    }

    @Override
    public byte[] generateCheckoutReceipt(Reservation reservation, Guest guest, Room room) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        document.add(new Paragraph("HotelTech - Comprobante de Salida", titleFont));
        document.add(new Paragraph("Reserva #" + reservation.reservationId(), bodyFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Huesped: " + guest.name() + " (Doc: " + guest.documentNumber() + ")", bodyFont));
        document.add(new Paragraph("Habitacion: " + room.number() + " (" + room.type().name() + ")", bodyFont));
        document.add(new Paragraph("Fecha de entrada: " + reservation.checkInDate(), bodyFont));
        document.add(new Paragraph("Fecha de salida: " + reservation.checkOutDate(), bodyFont));
        document.add(new Paragraph("Noches: " + reservation.getNights(), bodyFont));
        document.add(new Paragraph("Precio acordado por noche: $" + reservation.pricePerNightAtBooking(), bodyFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("TOTAL A PAGAR: $" + reservation.calculateTotalPrice(), titleFont));

        document.close();
        return out.toByteArray();
    }
}