package ru.practicum.shareit.request.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestRequestDto {
    @NotNull
    @Size(min = 0, message = "Description must not be empty")
    private String description;

}
