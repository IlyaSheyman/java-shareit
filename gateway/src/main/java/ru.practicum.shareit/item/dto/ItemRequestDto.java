package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Optional;

@Data
public class ItemRequestDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Optional<Boolean> available;
    @PositiveOrZero
    private Integer requestId;
}