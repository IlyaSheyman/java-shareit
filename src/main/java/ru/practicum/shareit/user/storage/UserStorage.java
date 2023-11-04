package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User add(User user);

    User update(int id, User user);

    User delete(int id);

    User get(int id);

    List<User> getAll();
}