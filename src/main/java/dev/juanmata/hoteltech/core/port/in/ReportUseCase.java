package dev.juanmata.hoteltech.core.port.in;

import java.time.LocalDate;

public interface ReportUseCase {
    byte[] generateReservationsPdf(LocalDate startDate, LocalDate endDate);
    byte[] generateCheckoutReceiptPdf(Long reservationId);
}