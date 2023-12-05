package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceDbImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("UserServiceDbImpl")
public class UserServiceDbImpl implements UserService {

    public final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ItemServiceDbImpl itemService;
    private final ItemRepository itemRepository;

    public UserServiceDbImpl(UserRepository userRepository,
                             ItemServiceDbImpl itemServiceDb,
                             ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.userMapper = new UserMapper();
        this.itemRepository = itemRepository;
        this.itemService = itemServiceDb;
    }

    @Override
    public UserDto addUser(User user) {
        if (user.getEmail() != null) {
            userRepository.save(user);
        } else {
            throw new ValidationException("Email не указан");
        }
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(int id, User user) {
        if (userRepository.getById(id) != null) {
            if (emailExists(user.getEmail(), id)) {
                throw new RuntimeException("Этот email уже занят");
            }

            User previous = userRepository.getById(id);

            if (user.getName() != null && user.getName() != previous.getName()) {
                previous.setName(user.getName());
            }

            if (previous.getEmail() != null
                    && user.getEmail() != null
                    && user.getEmail() != previous.getEmail()) {

                previous.setEmail(user.getEmail());
            }

            userRepository.save(previous);

            return userMapper.toUserDto(previous);
        } else {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    private boolean emailExists(String email, int id) {
        for (User u : userRepository.findAll()) {
            if (u.getId() != id) {
                if (u.getEmail().equals(email)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public UserDto getUser(int id) {
        try {
            if (userRepository.getById(id) != null) {
                return userMapper.toUserDto(userRepository.getById(id));
            } else {
                throw new NotFoundException("Пользователь с id " + id + " не найден");
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User u : userRepository.findAll()) {
            usersDto.add(userMapper.toUserDto(u));
        }
        return usersDto;
    }

    @Override
    public UserDto delete(int id) {
        for (Item item: itemRepository.findAll()) {
            if (item.getOwner().getId() == id) {
                itemService.deleteItem(item.getId(), id);
            }
        }
        User userToDelete = userRepository.getById(id);
        userRepository.delete(userToDelete);

        return userMapper.toUserDto(userToDelete);
    }
}
