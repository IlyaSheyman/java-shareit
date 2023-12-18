package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

@Data
public class UserRequestDto {
    @PositiveOrZero
    private int id;
    @Email
    private String email;
    @NotEmpty
    private String name;
}
