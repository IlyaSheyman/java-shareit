package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    public UserDto addUser(User user);

    public UserDto updateUser(int id, User user);

    public UserDto getUser(int id);

    public List<UserDto> getUsers();

    public UserDto delete(int id);
}