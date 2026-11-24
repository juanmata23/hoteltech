package dev.juanmata.hoteltech.adapter.persistence;

import dev.juanmata.hoteltech.core.constant.UserRole;
import dev.juanmata.hoteltech.core.constant.UserStatus;
import dev.juanmata.hoteltech.core.entity.User;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterConstructorMapper(User.class)
public interface UserSqlDao {

    @SqlUpdate("INSERT INTO user (name, email, password, role, status) VALUES (:name, :email, :password, :role, :status)")
    @GetGeneratedKeys("user_id")
    long insert(@Bind("name") String name, @Bind("email") String email, @Bind("password") String password,
                @Bind("role") UserRole role, @Bind("status") UserStatus status);

    @SqlUpdate("UPDATE user SET name = :name, email = :email, password = :password, role = :role, status = :status WHERE user_id = :userId")
    void update(@Bind("userId") Long userId, @Bind("name") String name, @Bind("email") String email,
                @Bind("password") String password, @Bind("role") UserRole role, @Bind("status") UserStatus status);

    @SqlQuery("SELECT user_id, name, email, password, role, status FROM user WHERE user_id = :userId")
    Optional<User> findById(@Bind("userId") Long userId);

    @SqlQuery("SELECT user_id, name, email, password, role, status FROM user WHERE email = :email")
    Optional<User> findByEmail(@Bind("email") String email);

    @SqlQuery("SELECT user_id, name, email, password, role, status FROM user")
    List<User> findAll();
}