package dev.juanmata.hoteltech.adapter.persistence;

import dev.juanmata.hoteltech.core.entity.User;
import dev.juanmata.hoteltech.core.port.out.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserJdbiAdapter implements UserRepository {

    private final UserSqlDao dao;

    public UserJdbiAdapter(UserSqlDao dao) {
        this.dao = dao;
    }

    @Override
    public User save(User user) {
        long id = dao.insert(user.name(), user.email(), user.password(), user.role(), user.status());
        return new User(id, user.name(), user.email(), user.password(), user.role(), user.status());
    }

    @Override
    public void update(User user) {
        dao.update(user.userId(), user.name(), user.email(), user.password(), user.role(), user.status());
    }

    @Override
    public Optional<User> findById(Long userId) {
        return dao.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return dao.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return dao.findAll();
    }
}