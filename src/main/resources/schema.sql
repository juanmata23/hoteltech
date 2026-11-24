PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS user (
                                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    name TEXT NOT NULL,
                                    email TEXT NOT NULL UNIQUE,
                                    password TEXT NOT NULL,
                                    role TEXT NOT NULL,
                                    status TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS room (
                                    room_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    number TEXT NOT NULL UNIQUE,
                                    type TEXT NOT NULL,
                                    capacity INTEGER NOT NULL,
                                    price_per_night NUMERIC NOT NULL,
                                    status TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS guest (
                                     guest_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     name TEXT NOT NULL,
                                     document_number TEXT NOT NULL UNIQUE,
                                     phone TEXT NOT NULL,
                                     email TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation (
                                           reservation_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                           check_in_date TEXT NOT NULL,
                                           check_out_date TEXT NOT NULL,
                                           status TEXT NOT NULL,
                                           price_per_night_at_booking NUMERIC NOT NULL,
                                           checked_in_at TEXT,
                                           checked_out_at TEXT,
                                           guest_id INTEGER NOT NULL,
                                           room_id INTEGER NOT NULL,
                                           registered_by_user_id INTEGER NOT NULL,
                                           FOREIGN KEY (guest_id) REFERENCES guest(guest_id),
    FOREIGN KEY (room_id) REFERENCES room(room_id),
    FOREIGN KEY (registered_by_user_id) REFERENCES user(user_id)
    );