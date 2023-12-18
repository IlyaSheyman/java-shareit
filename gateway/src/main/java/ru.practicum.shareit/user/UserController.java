package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RequestMapping(path = "/users")
@RestController
public class UserController {

    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserRequestDto user) {
        log.info("Получен запрос на добавление пользователя.");
        return userClient.addUser(user);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUsers(@RequestBody UserRequestDto user, @PathVariable @PositiveOrZero int id) {
        log.info("Получен запрос на обновление пользователя с ID - {}.", id);
        return userClient.updateUser(id, user);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable @PositiveOrZero int id) {
        log.info("Получен запрос на получение пользователя с ID - {}.", id);
        return userClient.getUser(id);
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable @PositiveOrZero int id) {
        log.info("Получен запрос на удаление пользователя с ID - {}.", id);
        return userClient.delete(id);
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return userClient.getUsers();
    }

}
