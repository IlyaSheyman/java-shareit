package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
        this.userMapper = new UserMapper();
    }

    public UserDto addUser(User user) {
        isValidUser(user);
        return userMapper.toUserDto(userStorage.add(user));
    }

    public UserDto updateUser(int id, User user) {
        return userMapper.toUserDto(userStorage.update(id, user));
    }

    public UserDto getUser(int id) {
        return userMapper.toUserDto(userStorage.get(id));
    }

    public List<UserDto> getUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User u : userStorage.getAll()) {
            usersDto.add(userMapper.toUserDto(u));
        }
        return usersDto;
    }

    public UserDto delete(int id) {
        return userMapper.toUserDto(userStorage.delete(id));
    }

    public void isValidUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Поле email должно быть заполнено");
        } else if (getUsers() != null && !getUsers().isEmpty()) {
            for (UserDto u : getUsers()) {
                if (u.getEmail().equals(user.getEmail())) {
                    throw new RuntimeException("Пользователь с email " + user.getEmail() + " уже существует");
                }
            }
        }
    }
}