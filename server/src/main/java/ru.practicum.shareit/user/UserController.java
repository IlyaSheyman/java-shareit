package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RequestMapping(path = "/users")
@RestController
public class UserController {

    @Autowired
    @Qualifier("UserServiceDbImpl")
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("UserServiceDbImpl") UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping
    public UserDto addUser(@RequestBody User user) {
        log.info("Получен запрос на добавление пользователя.");
        return userService.addUser(user);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public UserDto updateUsers(@RequestBody User user, @PathVariable int id) {
        log.info("Получен запрос на обновление пользователя с ID - {}.", id);
        return userService.updateUser(id, user);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        log.info("Получен запрос на получение пользователя с ID - {}.", id);
        return userService.getUser(id);
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable int id) {
        log.info("Получен запрос на удаление пользователя с ID - {}.", id);
        return userService.delete(id);
    }

    @ResponseBody
    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return userService.getUsers();
    }

}
