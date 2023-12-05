package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserDtoSmall {
    private int id;

    public UserDtoSmall(int id) {
        this.id = id;
    }
}
