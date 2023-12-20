package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users;
    private int seq = 0;

    public InMemoryUserStorage() {
        this.users = new HashMap<>();
    }

    @Override
    public User add(User user) {
        seq++;
        user.setId(seq);
        users.put(seq, user);
        return user;
    }

    @Override
    public User update(int id, User user) {
        if (users.containsKey(id)) {
            User previous = users.remove(id);

            if (user.getName() != null && user.getName() != previous.getName()) {
                previous.setName(user.getName());
            }

            if (previous.getEmail() != null
                    && user.getEmail() != null
                    && user.getEmail() != previous.getEmail()) {

                if (emailExists(user.getEmail(), id)) {
                    users.put(id, previous);
                    throw new RuntimeException("Этот email уже занят");
                }

                previous.setEmail(user.getEmail());
            }

            users.put(id, previous);

            return previous;
        } else {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    private boolean emailExists(String email, int id) {
        for (User u : users.values()) {
            if (u.getId() != id) {
                if (u.getEmail().equals(email)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public User delete(int id) {
        if (users.containsKey(id)) {
            return users.remove(id);
        } else {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    @Override
    public User get(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    @Override
    public List<User> getAll() {
        List<User> usersValues = new ArrayList<>();
        for (User u : users.values()) {
            usersValues.add(u);
        }
        return usersValues;
    }
}
