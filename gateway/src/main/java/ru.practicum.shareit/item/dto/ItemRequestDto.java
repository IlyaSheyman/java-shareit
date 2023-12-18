package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Optional;

@Data
public class ItemRequestDto {
    @Size(min = 2, message = "Name must not be empty")
    private String name;

    @Size(min = 3, message = "Description must not be empty")
    private String description;

    private Optional<Boolean> available;

    @PositiveOrZero
    private Integer requestId;
}