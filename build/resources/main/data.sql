INSERT OR IGNORE INTO user (user_id, name, email, password, role, status) VALUES
(1, 'Administrador General', 'admin@hoteltech.com', 'admin123', 'ADMIN', 'ACTIVE'),
(2, 'Laura Recepcion', 'recepcion@hoteltech.com', 'recep123', 'RECEPTIONIST', 'ACTIVE');

INSERT OR IGNORE INTO room (room_id, number, type, capacity, price_per_night, status) VALUES
(1, '101', 'SINGLE', 1, 90000, 'AVAILABLE'),
(2, '102', 'DOUBLE', 2, 150000, 'AVAILABLE'),
(3, '201', 'SUITE', 4, 280000, 'AVAILABLE'),
(4, '202', 'DOUBLE', 2, 150000, 'MAINTENANCE');

INSERT OR IGNORE INTO guest (guest_id, name, document_number, phone, email) VALUES
(1, 'Carlos Mendoza', '1049623451', '3104567890', 'carlos.mendoza@correo.com'),
(2, 'Ana Sofia Rojas', '1052389012', '3209876543', 'ana.rojas@correo.com');

INSERT OR IGNORE INTO reservation (reservation_id, check_in_date, check_out_date, status, price_per_night_at_booking, checked_in_at, checked_out_at, guest_id, room_id, registered_by_user_id) VALUES
(1, '2026-11-25', '2026-11-28', 'PENDING', 150000, NULL, NULL, 1, 2, 2);