package dev.juanmata.hoteltech;

import dev.juanmata.hoteltech.adapter.pdf.OpenPdfReportAdapter;
import dev.juanmata.hoteltech.adapter.persistence.*;
import dev.juanmata.hoteltech.adapter.rest.JavalinRestApi;
import dev.juanmata.hoteltech.core.port.out.*;
import dev.juanmata.hoteltech.core.service.*;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Jdbi jdbi() throws Exception {
        Jdbi jdbi = Jdbi.create("jdbc:sqlite:hoteltech.db?date_string_format=yyyy-MM-dd");
        jdbi.installPlugin(new SqlObjectPlugin());

        // Ejecuta schema.sql y data.sql al iniciar
        String schema = new String(getClass().getResourceAsStream("/schema.sql").readAllBytes());
        String data = new String(getClass().getResourceAsStream("/data.sql").readAllBytes());

        jdbi.useHandle(handle -> {
            handle.createScript(schema).execute();
            handle.createScript(data).execute();
        });

        return jdbi;
    }
    @Bean
    public UserRepository userRepository(Jdbi jdbi) {
        return new UserJdbiAdapter(jdbi.onDemand(UserSqlDao.class));
    }

    @Bean
    public RoomRepository roomRepository(Jdbi jdbi) {
        return new RoomJdbiAdapter(jdbi.onDemand(RoomSqlDao.class));
    }

    @Bean
    public GuestRepository guestRepository(Jdbi jdbi) {
        return new GuestJdbiAdapter(jdbi.onDemand(GuestSqlDao.class));
    }

    @Bean
    public ReservationRepository reservationRepository(Jdbi jdbi) {
        return new ReservationJdbiAdapter(jdbi.onDemand(ReservationSqlDao.class));
    }

    @Bean
    public ReportGateway reportGateway() {
        return new OpenPdfReportAdapter();
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public RoomService roomService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        return new RoomService(roomRepository, reservationRepository);
    }

    @Bean
    public GuestService guestService(GuestRepository guestRepository) {
        return new GuestService(guestRepository);
    }

    @Bean
    public ReservationService reservationService(ReservationRepository reservationRepository,
                                                 RoomRepository roomRepository,
                                                 GuestRepository guestRepository,
                                                 UserRepository userRepository,
                                                 RoomService roomService) {
        return new ReservationService(reservationRepository, roomRepository, guestRepository, userRepository, roomService);
    }

    @Bean
    public ReportService reportService(ReservationRepository reservationRepository,
                                       GuestRepository guestRepository,
                                       RoomRepository roomRepository,
                                       ReportGateway reportGateway) {
        return new ReportService(reservationRepository, guestRepository, roomRepository, reportGateway);
    }

    @Bean
    public JavalinRestApi javalinRestApi(RoomService roomService, GuestService guestService, ReservationService reservationService) {
        JavalinRestApi restApi = new JavalinRestApi(roomService, guestService, reservationService);
        restApi.start(7070);
        return restApi;
    }
}